package RoboBluetoothClient.androidApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SoundMicOptionActivity extends BaseActivity {

    private boolean mSoundEnabled;
    private boolean mMicEnabled;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Toast.makeText(getApplicationContext(), "Received options!", Toast.LENGTH_LONG).show();
            Log.d("receiver", "Got message: " + message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sound_and_mic);
        mSoundEnabled = ClientApplication.getApplication().getRoboSettings().getSound() == 1;
        mMicEnabled = ClientApplication.getApplication().getRoboSettings().getMic() == 1;

        Switch enableSound = findViewById(R.id.switch_sound);
        Switch enableMic = findViewById(R.id.switch_mic);

        enableSound.setChecked(mSoundEnabled);
        enableMic.setChecked(mMicEnabled);

        enableSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean ret = setSoundConfig(b);
            }
        });

        enableMic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean ret = setMicConfig(b);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
    }

    private boolean setSoundConfig(boolean enable){
        CommunicationsTask connection = ClientApplication.getApplication().getCurrentBluetoothConnection();
        int enableByte = enable == true? 1 : 0;
        connection.write("/sound/" + enableByte);
        return false;
    }

    private boolean setMicConfig(boolean enable){
        CommunicationsTask connection = ClientApplication.getApplication().getCurrentBluetoothConnection();
        int enableByte = enable == true? 1 : 0;
        connection.write("/mic/" + enableByte);
        return false;
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
