#ifndef PhotoSensitiveSensor_H
#define PhotoSensitiveSensor_H
#include "Arduino.h"
#include "SaveStateSensor.h"

class PhotoSensitiveSensor : public SaveStateSensor
{
public:
    PhotoSensitiveSensor();
    ~PhotoSensitiveSensor();

    int read();
    void init(int sensorPin);
    float normalizeData();

private:
    int mSensorPin;
};
#endif
