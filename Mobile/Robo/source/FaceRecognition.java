package RoboBluetoothClient.androidApp;

import android.graphics.Bitmap;

public class FaceRecognition {
    private String name;
    private Bitmap bitmap;
    private Integer number;

    public FaceRecognition(String name, Bitmap bitmap, Integer number) {
        this.name = name;
        this.bitmap = bitmap;
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
