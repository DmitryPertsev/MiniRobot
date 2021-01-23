#include "BatteryChargeMeasurer.h"

BatteryChargeMeasurer::BatteryChargeMeasurer() : mVoltage(0)
{
}

BatteryChargeMeasurer::BatteryChargeMeasurer(int voltPin) : mVoltPint(voltPin), mVoltage(0)
{
}

BatteryChargeMeasurer::~BatteryChargeMeasurer()
{
}

void BatteryChargeMeasurer::init(int voltPin)
{
    mVoltPint = voltPin;
}

float BatteryChargeMeasurer::read() 
{
    float temp = (float)analogRead(mVoltPint);
    Serial.println(temp);
    mVoltage = ((temp) * 4.4) / 1024;
    Serial.println(mVoltage);
    mVoltage = mVoltage  * ((R1 + R2) / R2);
    Serial.println(mVoltage);
    return mVoltage;
}

float BatteryChargeMeasurer::getCurrentState()
{
    return mVoltage;
}

