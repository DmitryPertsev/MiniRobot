#ifndef MovementController_H
#define MovementController_H
#include "Arduino.h"
#include "MotorMovementCommand.h"
#include "InfraSensor.h"
#include "UltraSoundSensor.h"
#include "PhotoSensitiveSensor.h"
#include "ServoMovementCommand.h"

#define ikCenter 14
#define ikLeft 15
#define ikRight 16
#define ikBack 17 

#define trigPin 10 
#define echoPin 9

#define lightSensorPin 18

class MovementController
{
public:
    MovementController();
    ~MovementController();

    void setInitialState();
    void run();

private:
    boolean checkUltraSoundSensor();
    int checkInfraSensors(boolean allowForwardMovement);

    ServoMovementCommand servoCommand;
    MotorMovementCommand motorCommand;
    UltraSoundSensor USSensor;
    PhotoSensitiveSensor PSSensor;
    InfraSensor IRSensor;
};

#endif
