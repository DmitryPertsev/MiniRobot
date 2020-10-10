package RoboBluetoothClient.androidApp;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class WiFiSettingsActivity extends BaseActivity {

    public static boolean sWifiSettingInProgress;
    Runnable mInitWifiRunnable;
    final byte mReadBlockSize = 33;
    TextView mSsidTextView;
    TextView mPskTextView;
    TextView mMessageTextView;
    int readBufferPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        mSsidTextView = findViewById(R.id.ssid_text);
        mPskTextView = findViewById(R.id.psk_text);
        mMessageTextView = findViewById(R.id.messages_text);

        Button startButton = findViewById(R.id.start_button);
        sWifiSettingInProgress = false;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssid = mSsidTextView.getText().toString();
                String psk = mPskTextView.getText().toString();
                mInitWifiRunnable = new wifiSettingHandler(ssid, psk);
                (new Thread(mInitWifiRunnable)).start();
            }
        });

    }

    private void writeOutput(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String currentText = mMessageTextView.getText().toString();
                mMessageTextView.setText(currentText + "\n" + text);
            }
        });
    }

    private void clearOutput() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageTextView.setText("");
            }
        });
    }

    private void waitReceiveMessage(InputStream mmInputStream, long timeout) throws IOException {
        int bytesAvailable;

        while (true) {
            bytesAvailable = mmInputStream.available();
            if (bytesAvailable > 0) {
                byte[] packetBytes = new byte[bytesAvailable];
                byte[] readBuffer = new byte[1024];
                mmInputStream.read(packetBytes);

                for (int i = 0; i < bytesAvailable; i++) {
                    byte b = packetBytes[i];

                    if (b == mReadBlockSize) {
                        byte[] encodedBytes = new byte[readBufferPosition];
                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                        final String data;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            data = new String(encodedBytes, StandardCharsets.US_ASCII);
                            writeOutput("Received:" + data);
                        }

                        return;
                    } else {
                        readBuffer[readBufferPosition++] = b;
                    }
                }
            }
        }
    }

    final class wifiSettingHandler implements Runnable {
        private String ssid;
        private String psk;

        public wifiSettingHandler(String ssid, String psk) {
            this.ssid = ssid;
            this.psk = psk;
        }

        public void run() {
            clearOutput();
            writeOutput("Starting config update.");

            sWifiSettingInProgress = true;
            try {
                BluetoothSocket mmSocket =ClientApplication.getApplication().getBluetoothSocket();
                if (!mmSocket.isConnected()) {
                    mmSocket.connect();
                    Thread.sleep(1000);
                }

                writeOutput("Connected.");

                OutputStream mmOutputStream = mmSocket.getOutputStream();
                final InputStream mmInputStream = mmSocket.getInputStream();

                ClientApplication.getApplication().getCurrentBluetoothConnection().write("/wifi_connect/");

                waitReceiveMessage(mmInputStream, -1);

                writeOutput("Sending SSID.");

                mmOutputStream.write(ssid.getBytes());
                mmOutputStream.flush();
                waitReceiveMessage(mmInputStream, -1);

                writeOutput("Sending PSK.");

                mmOutputStream.write(psk.getBytes());
                mmOutputStream.flush();
                waitReceiveMessage(mmInputStream, -1);

                writeOutput("Success.");

            } catch (Exception e) {
                e.printStackTrace();

                writeOutput("Failed.");
            }

            sWifiSettingInProgress = false;
            writeOutput("Done.");
        }
    }
}