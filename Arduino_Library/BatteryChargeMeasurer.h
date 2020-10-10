#ifndef BatteryChargeSensor_H
#define BatteryChargeSensor_H

#include "Arduino.h"
#define R1 100000             
#define R2 51000

class BatteryChargeMeasurer
{
public:
    BatteryChargeMeasurer();
    BatteryChargeMeasurer(int voltPin);
    ~BatteryChargeMeasurer();

    void init(int voltPin);
    float read();
    float getCurrentState();

private:
    int mVoltPint;
    float mVoltage;
};

#endif
