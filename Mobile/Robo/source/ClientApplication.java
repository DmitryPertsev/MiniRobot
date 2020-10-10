package RoboBluetoothClient.androidApp;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;


public class ClientApplication extends Application {
    private static ClientApplication sInstance;
    protected CommunicationsTask mBluetoothConnection;
    private Settings mRoboSettings;
    private boolean mRoboSettingsLoaded;

    public static ClientApplication getApplication() {
        return sInstance;
    }

    public void onCreate() {
        super.onCreate();

        sInstance = this;
        mRoboSettings = new Settings();
        mRoboSettingsLoaded = false;
    }

    public CommunicationsTask getCurrentBluetoothConnection()
    {
        return mBluetoothConnection;
    }

    public void setCurrentBluetoothConnection(CommunicationsTask connection){
        mBluetoothConnection = connection;
    }

    public BluetoothSocket getBluetoothSocket(){
        if(mBluetoothConnection != null)
            return mBluetoothConnection.getBluetoothSocket();
        else
            return null;
    }

    public Settings getRoboSettings() {
        if(mRoboSettingsLoaded == false) {
            loadRoboSettingsFromPreferences();
            mRoboSettingsLoaded = true;
        }
        return mRoboSettings;
    }

    public void setRoboSettings(Settings mRoboSettings) {
        this.mRoboSettings = mRoboSettings;
        saveRoboSettingsInPreferences();
    }

    private void savePreferences(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                Constants.APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private int loadPreferences(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                Constants.APP_PREFERENCES, MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    private void loadRoboSettingsFromPreferences(){
        mRoboSettings.setSound(loadPreferences(Constants.APP_PREFERENCES_SOUND));
        mRoboSettings.setMic(loadPreferences(Constants.APP_PREFERENCES_MIC));
        mRoboSettings.setFacerec(loadPreferences(Constants.APP_PREFERENCES_FACEREC));
        mRoboSettings.setSubmon(loadPreferences(Constants.APP_PREFERENCES_SUBMON));
        mRoboSettings.setOpmode(loadPreferences(Constants.APP_PREFERENCES_OPMODE));
        mRoboSettings.setBattery(loadPreferences(Constants.APP_PREFERENCES_BATTERY));
    }

    private void saveRoboSettingsInPreferences(){
        savePreferences(Constants.APP_PREFERENCES_SOUND,mRoboSettings.getSound());
        savePreferences(Constants.APP_PREFERENCES_MIC,mRoboSettings.getMic());
        savePreferences(Constants.APP_PREFERENCES_FACEREC,mRoboSettings.getFacerec());
        savePreferences(Constants.APP_PREFERENCES_SUBMON,mRoboSettings.getSubmon());
        savePreferences(Constants.APP_PREFERENCES_OPMODE,mRoboSettings.getOpmode());
        savePreferences(Constants.APP_PREFERENCES_BATTERY,mRoboSettings.getBattery());
    }
}