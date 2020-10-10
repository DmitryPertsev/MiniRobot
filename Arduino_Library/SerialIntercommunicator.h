#ifndef SerialIntercommunicator_H
#define SerialIntercommunicator_H

#include "Arduino.h"
#include "BatteryController.h"
#include "LocationAnalysisCommand.h"
#include "MotorMovementCommand.h"
#include "ServoMovementCommand.h"

class SerialIntercommunicator
{
 public: 
     SerialIntercommunicator();
     ~SerialIntercommunicator();

     void run();
     boolean read();
     void write(const char * data);
     void write(const byte data);

private:
    String mCommand;
    String mParameter;

    LocationAnalysisCommand locationCommand;
    ServoMovementCommand servoCommand;
    MotorMovementCommand motorCommand;
    BatteryController batteryController;
};
#endif
