/*
Bluetooth communications task

Extends AsyncTask to support threaded interaction with a sever over a Bluetooth socket.

Copyright 2018  Gunnar Bowman, Emily Boyes, Trip Calihan, Simon D. Levy, Shepherd Sims

MIT License
 */

package RoboBluetoothClient.androidApp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class CommunicationsTask extends AsyncTask<Void, Void, Void> {

    private static final UUID deviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean mConnected = true;
    private ProgressDialog mProgressDialog;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket mBluetoothSocket;
    private String mAddress;

    CommunicationsTask(AppCompatActivity activity, String address, BluetoothSocket socket) {
        mCurrentActivity = activity;
        mBluetoothSocket = socket;
        mAddress =  address;
    }

    CommunicationsTask(String address, BluetoothSocket socket) {
        mBluetoothSocket = socket;
        mAddress =  address;
    }

    private void reconnect()
    {
        try {
            if (mBluetoothSocket == null || !mConnected) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mAddress);//connects to the device's address and checks if it's available
                mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(deviceUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                mBluetoothSocket.connect();//start connection
            }
        }
        catch (IOException e) {
            mConnected = false;//if the try failed, you can check the exception here
        }
    }

    @Override
    protected void onPreExecute()     {
        if ( mCurrentActivity != null)
            mProgressDialog = ProgressDialog.show(mCurrentActivity, "Connecting...", "wait we're almost there");  //show a progress dialog
    }

    @Override
    protected Void doInBackground(Void... devices) { //while the progress dialog is shown, the connection is done in background

        try {
            if (mBluetoothSocket == null || !mConnected) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mAddress);//connects to the device's address and checks if it's available
                mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(deviceUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                mBluetoothSocket.connect();//start connection
            }
        }
        catch (IOException e) {
            mConnected = false;//if the try failed, you can check the exception here
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) { //after the doInBackground, it checks if everything went fine

        super.onPostExecute(result);

        if ( mCurrentActivity != null) {
            mProgressDialog.dismiss();

            if(!mConnected) {
                popup("Connection Failed. Is it a SPP Bluetooth running a server? Try again.");
                mCurrentActivity.finish();
            }
            else
            {
                popup("Connected.");
            }
        }
    }

    public void write(String s) {

        try {
            mBluetoothSocket.getOutputStream().write(s.getBytes(),0,s.getBytes().length);
        }
        catch (IOException e) {
            popup("WriteText error.");
            if(e.getMessage().equals("Broken pipe")) {
                disconnect();
                reconnect();
            }
        }
    }

    public void write(byte b) {

        try {
            mBluetoothSocket.getOutputStream().write(b);
        }
        catch (IOException e) {
//            reconnect();
        }
    }

    public int read() {

        int i = -1;

        try {
            i = mBluetoothSocket.getInputStream().read();
        }
        catch (IOException e) {
            popup("ReadText error.");
//            reconnect();
        }

        return i;
    }

    public int read(byte []  buffer) {

        int i = -1;

        try {
            i = mBluetoothSocket.getInputStream().read(buffer);
        }
        catch (IOException e) {
            popup("ReadText error.");
//            reconnect();
        }

        return i;
    }

    public int read(byte []  buffer , int off, int len) {

        int i = -1;

        try {
            i = mBluetoothSocket.getInputStream().read(buffer,off, len);
        }
        catch (IOException e) {
            popup("ReadText error.");
//            reconnect();
        }

        return i;
    }

    public int isAvailable() {

        int n = 0;

        try {
            n = mBluetoothSocket.getInputStream().available();
        }
        catch (IOException e) {
        }

        return n;
    }

    public boolean isConnected() {
        if(mBluetoothSocket == null)
            return false;
        return mBluetoothSocket.isConnected();
    }

    public void disconnect() {

        if (mBluetoothSocket!=null)
        {
            try  {
                mBluetoothSocket.close(); //close connection
                mBluetoothSocket = null;
            }
            catch (IOException e) {
                popup("Error");
            }
        }

        popup("Disconnected");
    }


    private void popup(String s) {
        Toast.makeText(mCurrentActivity.getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }

    public BluetoothSocket getBluetoothSocket()
    {
        return mBluetoothSocket;
    }

    @SuppressLint("StaticFieldLeak")
    private AppCompatActivity mCurrentActivity = null;
}
