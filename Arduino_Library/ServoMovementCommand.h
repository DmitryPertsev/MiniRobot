#ifndef ServoMovementCommand_H
#define ServoMovementCommand_H

#include "Arduino.h"
#include "InteractionCommand.h"
//#include "Servo.h"   // Подключаем библиотеку для сервоприводом
#include <Adafruit_PWMServoDriver.h>

static Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();
//static Servo myservoHorizontal;  //поворот влево/вправо на шилде внутренние пины
//static Servo myservoVertical;  //поворот вверх/вниз на шилде наружние пины
static boolean started = false;
static int prevDegVert = 0;
static int prevDegHor = 0;
#define SERVOMIN  150 
#define SERVOMAX  550 
#define SERVOPORT0 1 
#define SERVOPORT1 0

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
