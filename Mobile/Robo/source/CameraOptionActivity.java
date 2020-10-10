package RoboBluetoothClient.androidApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class CameraOptionActivity extends BaseActivity {

    protected CommunicationsTask mBluetoothConnection;
    private SpinnerDropDown mSpinner;
    private FaceRecognitionAdapter mSubjectsAdapter;
    private boolean mFacerec;
    private boolean mSubjectmon;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Toast.makeText(getApplicationContext(), "Received options!", Toast.LENGTH_LONG).show();
            Log.d("receiver", "Got message: " + message);
        }
    };

    DialogInterface.OnClickListener dialogRemoveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    boolean alertDialogAnswer = true;
                    if(alertDialogAnswer && mSubjectsAdapter.getCount() > 1) {
                        deleteSubject((int) mSpinner.getSelectedItemId());
                        mSubjectsAdapter.remove((FaceRecognition) mSpinner.getSelectedItem());
                        mSubjectsAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "You cannot remove all subjects" +
                                " from recognition script. To stop face recognition use " +
                                "'Enable face recognition'", Toast.LENGTH_LONG).show();
                    }
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    alertDialogAnswer = false;
                    break;
            }
        }
    };

    DialogInterface.OnClickListener dialogAddClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    addNewSubject();
                    mSubjectsAdapter.add(new FaceRecognition("subject", null, mSubjectsAdapter.getCount()));
                    mSubjectsAdapter.notifyDataSetChanged();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    public static void setImageViewWithByteArray(ImageView view, byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        view.setImageBitmap(bitmap);
    }

    public void deleteSubject(int numberOfSubject){
        CommunicationsTask connection = ClientApplication.getApplication().getCurrentBluetoothConnection();
        connection.write("/remove/" + numberOfSubject);
    }

    public void addNewSubject(){
        CommunicationsTask connection = ClientApplication.getApplication().getCurrentBluetoothConnection();
        connection.write("/add/");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mFacerec = ClientApplication.getApplication().getRoboSettings().getFacerec() == 1;
        mSubjectmon = ClientApplication.getApplication().getRoboSettings().getSubmon() == 1;

        Switch enableFacerec = findViewById(R.id.switch_facerec);
        Switch enableSubmon = findViewById(R.id.switch_subjectmon);

        enableFacerec.setChecked(mFacerec);
        enableSubmon.setChecked(mSubjectmon);
        //load from dictionary;
        mBluetoothConnection = ClientApplication.getApplication().getCurrentBluetoothConnection();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        ImageButton addButton = findViewById(R.id.add_subject_button);
        final AlertDialog.Builder addAlert = new AlertDialog.Builder(addButton.getContext());
        addAlert.setMessage("Are you sure to add new subject? " +
                "Robo starts collect face images from its camera. " +
                "Please stay in front of the Robo to complete add operation until sound signal")
                .setPositiveButton("Yes", dialogAddClickListener)
                .setNegativeButton("No", dialogAddClickListener);
        addButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlert.create().show();
                ClientApplication.getApplication().getCurrentBluetoothConnection().write("/add/");
            }
        });

        mSpinner = findViewById(R.id.subjects_spinner);
        mSpinner.setSpinnerEventsListener(new SpinnerDropDown.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened(Spinner spinner) {
                if(mSubjectsAdapter.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "No subjects received", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onSpinnerClosed(Spinner spinner) {
                ImageManager.stopAllDownloads();
            }
        });
        mSubjectsAdapter = new FaceRecognitionAdapter(this, R.layout.face_item, new ArrayList<FaceRecognition>());
        final AlertDialog.Builder builder = new AlertDialog.Builder(mSubjectsAdapter.getContext());
        builder.setMessage("Are you sure to delete this subject?").setPositiveButton("Yes", dialogRemoveClickListener)
                .setNegativeButton("No", dialogRemoveClickListener);
        mSubjectsAdapter.setOwner(mSpinner);
        mSubjectsAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                mSpinner.setSelection(i);
                builder.create().show();
                return false;
            }
        });

        mSubjectsAdapter.setAdapterEventsListener(new FaceRecognitionAdapter.OnAdapterEventsListener() {
            @Override
            public void onGetDropDownView(ImageView image, Integer number) {
                ImageManager.startDownload(image,number,true);
            }

            @Override
            public void onGetView(ImageView image, Integer number) {

            }

        });
        mSpinner.setAdapter(mSubjectsAdapter);

        Switch enableFaceRec = findViewById(R.id.switch_facerec);
        Switch enableSubjectMon = findViewById(R.id.switch_subjectmon);

        enableFaceRec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setFaceRecConfig(b);
                mFacerec = b;
            }
        });
        enableSubjectMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setSubjectMonConfig(b);
                mSubjectmon = b;
            }
        });
    }

    private boolean setFaceRecConfig(boolean enable){
        CommunicationsTask connection = ClientApplication.getApplication().getCurrentBluetoothConnection();
        int enableByte = enable == true? 1 : 0;
        connection.write("/facerec/" + enableByte);
        return false;
    }

    private boolean setSubjectMonConfig(boolean enable){
        CommunicationsTask connection = ClientApplication.getApplication().getCurrentBluetoothConnection();
        int enableByte = enable == true? 1 : 0;
        connection.write("/submon/" + enableByte);
        return false;
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
