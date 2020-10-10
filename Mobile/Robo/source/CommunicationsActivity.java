/*
Bluetooth communications activity

Works with BluetoothConnection to provide simple interaction with a sever over a Bluetooth socket:
seek bar (slider) sends serialized values to server; activity checks for available responses from
server.

Copyright 2018  Gunnar Bowman, Emily Boyes, Trip Calihan, Simon D. Levy, Shepherd Sims

MIT License
*/

package RoboBluetoothClient.androidApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class CommunicationsActivity extends AppCompatActivity {

    protected String mDeviceAddress;
    protected CommunicationsTask mBluetoothConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communications);

        // Retrieve the address of the bluetooth device from the BluetoothListDeviceActivity
        Intent newint = getIntent();
        mDeviceAddress = newint.getStringExtra("device_address");
        initializeBluetoothConnection();
    }

    @Override
    public void onDestroy() {
        //mBluetoothConnection.disconnect();
        //mBluetoothConnection = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mBluetoothConnection.disconnect();
        mBluetoothConnection = null;
        super.onBackPressed();
    }

    private void initializeBluetoothConnection(){
        mBluetoothConnection = new CommunicationsTask(this, mDeviceAddress, ClientApplication.getApplication().getBluetoothSocket());
        mBluetoothConnection.execute();
        ClientApplication.getApplication().setCurrentBluetoothConnection(mBluetoothConnection);
    }
}
