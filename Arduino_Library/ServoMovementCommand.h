#ifndef ServoMovementCommand_H
#define ServoMovementCommand_H

#include "Arduino.h"
#include "InteractionCommand.h"
#include <Adafruit_PWMServoDriver.h>

static Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();
static boolean started = false;
#define SERVOMIN  150 
#define SERVOMAX  550 
#define SERVOPORT0 9 
#define SERVOPORT1 8

class ServoMovementCommand : public InteractionCommand
{
public:
    ServoMovementCommand();
    ~ServoMovementCommand();

    void execute();
    ServoMovementCommand * init(int servoNum, int deg);

private:
    int mDeg;
    int mServoNum;

    void servoVertical(int deg);
    void servoHorizontal(int deg);
};

#endif
