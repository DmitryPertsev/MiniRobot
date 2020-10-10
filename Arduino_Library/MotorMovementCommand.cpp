#include "MotorMovementCommand.h"

boolean MotorMovementCommand::running = false;
int MotorMovementCommand::currentDirection = -1;
int MotorMovementCommand::speed = 100;

MotorMovementCommand::MotorMovementCommand() : mTimeDelay(0)
{
    setSpeed(100);
    currentDirection = -1;
    Serial.println("MotorMovementCommand loaded");
}

MotorMovementCommand::MotorMovementCommand(int direction, long timeDelay) :  mTimeDelay(timeDelay) 
{
    setSpeed(100);
    currentDirection = direction;
    Serial.println("MotorMovementCommand loaded");
}

MotorMovementCommand::~MotorMovementCommand() 
{
}

void MotorMovementCommand::execute()
{
    switch (currentDirection)
    {
    case -1:
        stop();
        break;
    case 0:
        turnLeft(mTimeDelay);
        break;
    case 1:
        goForward(mTimeDelay);
        break;
    case 2:
        turnRight(mTimeDelay);
        break;
    case 3:
        goBack(mTimeDelay);
    }
}

MotorMovementCommand * MotorMovementCommand::init(int direction, long timeDelay)
{
    currentDirection = direction;
    mTimeDelay = timeDelay;
    return this;
}

void MotorMovementCommand::stop() 
{
    MotorMovementCommand::running = false;
    motor1.run(RELEASE);
    motor2.run(RELEASE);
    motor3.run(RELEASE);
    motor4.run(RELEASE);
    delay(1000);
}

void MotorMovementCommand::goForward(int time) 
{
    MotorMovementCommand::running = true;
    forward();
    if (time)
    {
        delay(time);
        stop();
    }
}

void MotorMovementCommand::goBack(long time) 
{
    MotorMovementCommand::running = true;
    back();
    if (time)
    {
        delay(time);
        stop();
    }
}

void MotorMovementCommand::turnRight(long time) 
{

    MotorMovementCommand::running = true;
    right();
    delay(time);
    stop();
}

void MotorMovementCommand::turnLeft(long time) 
{
    MotorMovementCommand::running = true;
    left();
    delay(time);
    stop();
}

void MotorMovementCommand::forward() 
{
    motor1.run(FORWARD);
    motor2.run(FORWARD);
    motor3.run(FORWARD);
    motor4.run(FORWARD);

    motor1.setSpeed(speed);
    motor2.setSpeed(speed);
    motor3.setSpeed(speed);
    motor4.setSpeed(speed);
}

void MotorMovementCommand::back() {
    motor1.run(BACKWARD);
    motor2.run(BACKWARD);
    motor3.run(BACKWARD);
    motor4.run(BACKWARD);

    motor1.setSpeed(speed);
    motor2.setSpeed(speed);
    motor3.setSpeed(speed);
    motor4.setSpeed(speed);
}

void MotorMovementCommand::left()
{
    motor1.run(BACKWARD);
    motor2.run(BACKWARD);
    motor3.run(FORWARD);
    motor4.run(FORWARD);

    motor1.setSpeed(speed);
    motor2.setSpeed(speed);
    motor3.setSpeed(speed);
    motor4.setSpeed(speed);
}

void MotorMovementCommand::right()
{
    motor1.run(FORWARD);
    motor2.run(FORWARD);
    motor3.run(BACKWARD);
    motor4.run(BACKWARD);

    motor1.setSpeed(speed);
    motor2.setSpeed(speed);
    motor3.setSpeed(speed);
    motor4.setSpeed(speed);
}