#ifndef UltraSoundSensor_H
#define UltraSoundSensor_H
#include "Arduino.h"
#include "SaveStateSensor.h"

class UltraSoundSensor : public SaveStateSensor
{
public:
    UltraSoundSensor();
    UltraSoundSensor(int trigPin, int echoPin);
    ~UltraSoundSensor();

    void init(int trigPin, int echoPin);
    int read();

private:
    int mTrigPin;
    int mEchoPin;
    long mDuration;
};

#endif
