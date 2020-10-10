#include "BatteryController.h"

BatteryController::BatteryController()
{
}

BatteryController::~BatteryController()
{
}

byte BatteryController::run() 
{
    float volts = 0;
    float k = 5;                  // correlation coefficient to the real current value of the voltage of the batteries
    for (int i = 0; i < 5; i++) 
    {
        volts += sensor.read();   //get the voltage of 5 in 10ns
    }

    int measure = 100 * (volts / (3 * 5));  //get the voltage*100 for 1 battery

    if (measure > 380)                      //full charge
        return map(measure, 420, 380, 100, 80);
    else if ((measure <= 380) && (measure > 375))
        return map(measure, 380, 375, 80, 50);
    else if ((measure <= 375) && (measure > 360))
        return map(measure, 375, 360, 50, 25);
    else if ((measure <= 360) && (measure > 340))
        return map(measure, 360, 340, 25, 8);
    else                                    //low charge
        return map(measure, 340, 260, 8, 0);
}