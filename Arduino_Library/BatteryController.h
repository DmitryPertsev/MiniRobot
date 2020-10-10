#ifndef BatteryController_H
#define BatteryController_H
#include "Arduino.h"
#include "BatteryChargeMeasurer.h"

#define VOLT_PIN 20

class BatteryController
{
public:
    BatteryController();
    ~BatteryController();

    byte run();

private:
    BatteryChargeMeasurer sensor = BatteryChargeMeasurer(VOLT_PIN);
};

#endif
