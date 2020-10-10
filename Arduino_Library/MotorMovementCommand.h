#ifndef MotorMovementCommand_h
#define MotorMovementCommand_h
#define DEFAULT_SPEED 100
#include "Arduino.h"
#include "InteractionCommand.h"
#include <AFMotor.h>

static AF_DCMotor motor1(1);
static AF_DCMotor motor2(2);
static AF_DCMotor motor3(3);
static AF_DCMotor motor4(4);

class MotorMovementCommand : public InteractionCommand
{
public:
    MotorMovementCommand();
    MotorMovementCommand(int direction, long timeDelay);
    ~MotorMovementCommand();

    static boolean isRunning()
    {
        return MotorMovementCommand::running;
    }

    static int getCurrentDirection()
    {
        return currentDirection;
    }

    static void setSpeed(int speed)
    {
        speed = speed;
    }
    static void setDefaultSpeed()
    {
        speed = DEFAULT_SPEED;
    }

    void execute();
    MotorMovementCommand * init(int direction, long timeDelay = 0);

private:
    static int speed;
    static int currentDirection;
    static boolean running;
    long mTimeDelay;

    void stop();

    void goForward(int time);
    void goBack(long time);
    void turnRight(long time);
    void turnLeft(long time);

    void forward();
    void back();
    void left();
    void right();
};

#endif 
