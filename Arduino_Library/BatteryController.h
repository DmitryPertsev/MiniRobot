#ifndef BatteryController_H
#define BatteryController_H
#include "Arduino.h"
#include "BatteryChargeMeasurer.h"

#define VOLT_PIN A0

class BatteryController
{
public:
    BatteryController();
    ~BatteryController();

    int run();

private:
    BatteryChargeMeasurer sensor = BatteryChargeMeasurer(VOLT_PIN);
};

#endif
