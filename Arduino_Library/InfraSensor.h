#ifndef InfraSensor_H
#define InfraSensor_H
#include "Arduino.h"
#include "SaveStateSensor.h"

class InfraSensor: public SaveStateSensor
{
public:
    InfraSensor();
    InfraSensor(int centerPin, int leftPin, int rightPin, int backPin);
    ~InfraSensor();

    void init(int center, int left, int right, int back);
    int read();
    int getCenterPinState();
    int getLeftPinState();
    int getRightPinState();
    int getBackPinState();
    int * getResultAsArray();

private:
    int mCenterPin;
    int mLeftPin;
    int mRightPin;
    int mBackPin;
};

#endif
