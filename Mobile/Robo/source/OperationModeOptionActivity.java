package RoboBluetoothClient.androidApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

public class OperationModeOptionActivity extends BaseActivity {

    private int mCurrentMode;
    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.radioButton: setOperationModeConfig(0);
                    break;
                case R.id.radioButton2: setOperationModeConfig(1);
                    break;

                default:
                    break;
            }
        }
    };
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            //Toast.makeText(getApplicationContext(), "Received options!", Toast.LENGTH_LONG).show();
            Log.d("receiver", "Got message: " + message);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentMode = ClientApplication.getApplication().getRoboSettings().getOpmode();
        setContentView(R.layout.activity_operation_mode);

        RadioButton standbyRadioButton = findViewById(R.id.radioButton);
        standbyRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton standaloneRadioButton = findViewById(R.id.radioButton2);
        standaloneRadioButton.setOnClickListener(radioButtonClickListener);

        if(mCurrentMode == 1)
        {
            standaloneRadioButton.toggle();
        }
        else
        {
            standbyRadioButton.toggle();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
    }

    private boolean setOperationModeConfig(int mode){
        CommunicationsTask connection = ClientApplication.getApplication().getCurrentBluetoothConnection();
        connection.write("/opmode/" + mode);
        return false;
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
