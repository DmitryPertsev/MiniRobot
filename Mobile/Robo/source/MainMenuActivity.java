package RoboBluetoothClient.androidApp;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public  class MainMenuActivity extends CommunicationsActivity {
    private final Handler mUpdateHandler = new Handler();
    private final Context mLocalContext = this;
    private LinearLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainLayout = findViewById(R.id.linear_layout);
        ListView listView = findViewById(R.id.optionsList);
        Intent newint = getIntent();
        mDeviceAddress = newint.getStringExtra("device_address");

        final String[] menuItems = getResources().getStringArray(R.array.menu_items);
        TypedArray icons = getResources().obtainTypedArray(R.array.menu_items_icons);

        ArrayList<MainMenuItem> itemsArray = new ArrayList<MainMenuItem>();
        int iconIndex = 0;
        for(String s : menuItems)
        {

            int resourceid = icons.getResourceId(iconIndex< icons.length()? iconIndex++ : 0 ,-1);
            itemsArray.add(new MainMenuItem(resourceid,s));
        }

        MainMenuAdapter adapter = new MainMenuAdapter(this,
                R.layout.main_menu_list_item, itemsArray);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                TextView textView = (TextView) itemClicked;
                String strText = textView.getText().toString();

                if(strText.equalsIgnoreCase(menuItems[0])) {
                    Intent i = new Intent(MainMenuActivity.this, CameraOptionActivity.class);
                    startActivityForResult(i,0);
                    strText.hashCode();
                }
                else if (strText.equalsIgnoreCase(menuItems[1]))
                {
                    Intent i = new Intent(MainMenuActivity.this, SoundMicOptionActivity.class);
                    startActivityForResult(i,0);
                }
                else if(strText.equalsIgnoreCase(menuItems[2])) {
                    Intent i = new Intent(MainMenuActivity.this, WiFiSettingsActivity.class);
                    i.putExtra("device_address", mDeviceAddress); //this will be received at CommunicationsActivity
                    startActivityForResult(i,2);
                }
                else if (strText.equalsIgnoreCase(menuItems[3]))
                {
                    Intent i = new Intent(MainMenuActivity.this, OperationModeOptionActivity.class);
                    startActivityForResult(i,0);
                }
                else
                {
                    Intent i = new Intent(MainMenuActivity.this, FindMyRobo.class);
                    startActivityForResult(i,0);
                }
            }
        });

        mUpdateHandler.postDelayed(mTimerRunnable, 2000); // 1 second delay (takes millis)
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                showPopupWindowClick();
                return true;
            case R.id.disconnect:
                mBluetoothConnection.disconnect();
                mBluetoothConnection = null;
                finish();
                return true;
            case R.id.exit:
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            case R.id.restart:
                restartRoboCommand();
                finish();
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void showPopupWindowClick() {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.about_window, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(mMainLayout, Gravity.CENTER, 0, 0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void updateRoboSettings(){
        if(ImageManager.sDownloadInProgress == false && WiFiSettingsActivity.sWifiSettingInProgress == false &&
                ClientApplication.getApplication().getCurrentBluetoothConnection().isConnected()) {

            ClientApplication.getApplication().getCurrentBluetoothConnection().write("/robot/");
            byte[] buffer = new byte[256];
            int bytes = mBluetoothConnection.read(buffer);
            if (bytes > 0) {
                String str = new String(buffer, 0, bytes);
                Log.d("sender", str);
                Gson gson = new Gson();
                try {
                    Settings bm = gson.fromJson(str, Settings.class);
                    if (bm.equals(ClientApplication.getApplication().getRoboSettings()) == false) {
                        Log.d("Save settings", "in preferences");
                        ClientApplication.getApplication().setRoboSettings(bm);
                        Intent intent = new Intent("custom-event-name");
                        // You can also include some extra data.
                        intent.putExtra("message", "This is my message!");
                        LocalBroadcastManager.getInstance(mLocalContext).sendBroadcast(intent);
                    }
                }
                catch(IllegalStateException e) {
                }
            }
        }
    }

    private void restartRoboCommand(){
        mBluetoothConnection.write("/restart/");
        mBluetoothConnection.disconnect();
        mBluetoothConnection = null;
    }

    @Override
    protected void onPause() {
        //mUpdateHandler.removeCallbacks(mTimerRunnable);
        super.onPause();
    }

    @Override
    protected void onResume() {
        //mUpdateHandler.postDelayed(mTimerRunnable, 2000);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mUpdateHandler.removeCallbacks(mTimerRunnable);
        super.onDestroy();
    }

    private Runnable mTimerRunnable = new Runnable() {
        private long time = 0;

        @Override
        public void run()
        {
            updateRoboSettings();
            time += 2000;
            mUpdateHandler.postDelayed(this, 2000);
        }
    };
}
