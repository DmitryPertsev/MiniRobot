#include "ServoMovementCommand.h"

ServoMovementCommand::ServoMovementCommand() : mServoNum(0), mDeg(90) 
{
    Serial.println("ServoMovementCommand loaded");
}

ServoMovementCommand::~ServoMovementCommand() 
{
}

void ServoMovementCommand::execute()
{
    if (!started)
    {
        pwm.begin();
        Serial.println("begin finished");
        pwm.setPWMFreq(60);
        Serial.println("set PWM finished");
        started = true;
        Serial.println("PWM loaded");
    }

    switch (mServoNum)
    {
    case 0:
        servoVertical(mDeg);
        break;
    case 1:
        servoHorizontal(mDeg);
        break;
    }
}

ServoMovementCommand * ServoMovementCommand::init(int servoNum, int deg)
{
    mServoNum = servoNum;
    mDeg = deg;
    return this;
}

void ServoMovementCommand::servoVertical(int deg) {
    int pulselength = map(deg, 0, 180, SERVOMIN, SERVOMAX);
    pwm.setPWM(SERVOPORT1, 0, pulselength); 
}

void ServoMovementCommand::servoHorizontal(int deg) {
    int pulselength = map(deg, 0, 180, SERVOMIN, SERVOMAX);
    pwm.setPWM(SERVOPORT0, 0, pulselength);
}