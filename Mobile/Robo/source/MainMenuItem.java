package RoboBluetoothClient.androidApp;

public class MainMenuItem {

    private int icon;
    private String text;

    public MainMenuItem(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public int getIconId()
    {
        return icon;
    }

    public String getTextString()
    {
        return text;
    }
}
