package RoboBluetoothClient.androidApp;

public class Settings {
    Integer sound;
    Integer mic;
    Integer facerec;
    Integer submon;
    //Integer[] subjects;
    Integer opmode;
    Integer battery;
    //String wifiadd;

    public Integer getSound() {
        return sound;
    }

    public void setSound(Integer sound) {
        this.sound = sound;
    }

    public Integer getMic() {
        return mic;
    }

    public void setMic(Integer mic) {
        this.mic = mic;
    }

    public Integer getFacerec() {
        return facerec;
    }

    public void setFacerec(Integer facerec) {
        this.facerec = facerec;
    }

    public Integer getSubmon() {
        return submon;
    }

    public void setSubmon(Integer submon) {
        this.submon = submon;
    }

//    public Integer[] getSubjects() {
//        return subjects;
//    }
//
//    public void setSubjects(Integer[] subjects) {
//        this.subjects = subjects;
//    }

    public Integer getOpmode() {
        return opmode;
    }

    public void setOpmode(Integer opmode) {
        this.opmode = opmode;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    @Override
    public String toString() {
        return "Settings{" +
                " sound=" + sound +
                " mic=" + mic +
                " facerec=" + facerec +
                " submon=" + submon +
                " opmode=" + opmode +
                " battery=" + battery +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return sound.equals(settings.sound) &&
                mic.equals(settings.mic) &&
                facerec.equals(settings.facerec) &&
                submon.equals(settings.submon) &&
                opmode.equals(settings.opmode) &&
                battery.equals(settings.battery);
    }

}
