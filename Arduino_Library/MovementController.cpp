#include "MovementController.h"
#define FORBID_FORWARD 1
#define FORBID_LEFT 2
#define FORBID_FORWARD_LEFT 3
#define FORBID_RIGHT 4
#define FORBID_FORWARD_RIGHT 5
#define FORBID_LEFT_RIGHT 6
#define FORBID_FORWARD_LEFT_RIGHT 7
#define FORBID_ALL 15

#define MOVE_STOP -1
#define MOVE_LEFT 0
#define MOVE_FORWARD 1
#define MOVE_RIGHT 2
#define MOVE_BACK 3

MovementController::MovementController()
{
    USSensor.init(trigPin, echoPin);
    PSSensor.init(lightSensorPin);
    IRSensor.init(ikCenter, ikLeft, ikRight, ikBack);
    Serial.println("Movement controller loaded");
    setInitialState();
}

void MovementController::setInitialState()
{
    motorCommand.init(-1)->execute();
    servoCommand.init(0, 25)->execute();
    servoCommand.init(1, 25)->execute();
}

MovementController::~MovementController() 
{
}

void MovementController::run()
{
    if (MotorMovementCommand::isRunning())
    {
        int lightLevel = PSSensor.read();

        //low lightning
        if (lightLevel < 512)
            MotorMovementCommand::setSpeed(100 * PSSensor.normalizeData());
        else
            MotorMovementCommand::setDefaultSpeed();

        boolean barrierInForward = checkUltraSoundSensor();
        int forbiddenDirection = checkInfraSensors(barrierInForward);

        //none 0 //break
        //center 1 // go back, turn right and forward
        //left 2 // go back, turn right and forward
        //center & left 3 // go back, turn right and forward
        //right 4 // go back, turn left and forward
        //center & right 5 // go back, left and forward
        //left & right 6 // go back and stop 
        //center & left & right 7 // go back and stop
        //back 8 // go forward

        switch (forbiddenDirection)
        {
        case 0:
            break;
        case FORBID_FORWARD: //forward
        case FORBID_LEFT:
        case FORBID_FORWARD_LEFT:
            if (MotorMovementCommand::getCurrentDirection() < 3)
            {
                motorCommand.init(MOVE_BACK, 500)->execute();
                motorCommand.init(MOVE_RIGHT, 200)->execute();
                motorCommand.init(MOVE_FORWARD)->execute();
            }
            break;
        case FORBID_RIGHT://center && right
        case FORBID_FORWARD_RIGHT:
            if (MotorMovementCommand::getCurrentDirection() < 3)
            {
                motorCommand.init(MOVE_BACK, 500)->execute();
                motorCommand.init(MOVE_LEFT, 200)->execute();
                motorCommand.init(MOVE_FORWARD)->execute();
            }
            break;
        case FORBID_LEFT_RIGHT://left & right
        case FORBID_FORWARD_LEFT_RIGHT://& center
            if (MotorMovementCommand::getCurrentDirection() < 3)
            {
                motorCommand.init(MOVE_BACK, 500)->execute();
                motorCommand.init(MOVE_STOP)->execute();
            }
            break;
        case FORBID_ALL:
            motorCommand.init(MOVE_STOP)->execute();
            break;
        default:
            if (MotorMovementCommand::getCurrentDirection() == 3)
            {
                motorCommand.init(MOVE_FORWARD, 500)->execute();
                motorCommand.init(MOVE_LEFT, 500)->execute();
                motorCommand.init(MOVE_BACK)->execute(); 
            }
            break;
        }
    }
}

boolean MovementController::checkUltraSoundSensor()
{
    int distance = USSensor.read();

    if (distance < 30)
        return false;
    return true;
}

int MovementController::checkInfraSensors(boolean allowForwardMovement)
{
    IRSensor.read();
    int * irSensorsState = IRSensor.getResultAsArray();
    int returnDirection = 0;
    int multiplier = 1;

    if (allowForwardMovement)
        irSensorsState[0] = HIGH;

    for (int index = 0; index < 4; index++)
    {
        returnDirection += irSensorsState[index] * multiplier;
        multiplier *= 2;
    }

    return returnDirection;
}