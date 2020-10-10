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
    mVoltage = ((float)analogRead(mVoltPint)) / 1024 * ((R1 + R2) / R2);
    return mVoltage;
}

float BatteryChargeMeasurer::getCurrentState()
{
    return mVoltage;
}

