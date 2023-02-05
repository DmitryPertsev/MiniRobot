//
// Created by Anzhalika Dziarkach on 03.04.2022.
//

#ifndef CONTROLCONTROLLER_H
#define CONTROLCONTROLLER_H

#include "pch.h"
#include "Arduino.h"

#define BASE_AXIS_VALUE 1800
#define AXIS_INFELICITY 350
#define MIN_AXIS_VALUE 1500
#define MAX_AXIS_VALUE 4095

const String FORWARD_MOVEMENT = "FORWARD";
const String BACK_MOVEMENT = "BACK";
const String RIGHT_MOVEMENT = "RIGHT";
const String LEFT_MOVEMENT = "LEFT";
const String UP_MOVEMENT = "UP";
const String DOWN_MOVEMENT = "DOWN";
const String STOP_MOVEMENT = "STOP";

class ControlController {
public:
    ControlController();
    ~ControlController();

    void turnOnLED();
    void turnOffLED();

    void turnOnSound();
    void turnOffSound();

    void turnOnVibro();
    void turnOffVibro();

    int getXPositionFromMovementJoystick();
    int getYPositionFromMovementJoystick();
    bool isMovementButtonPressed();

    int getXPositionFromCameraJoystick();
    int getYPositionFromCameraJoystick();
    bool isCameraButtonPressed();

    String getMovementDirection(int x, int y);
    int getMovementSpeed(int x, int y);

    String getCameraHorizontalDirection(int x, int y);
    String getCameraVerticalDirection(int x, int y);
};


#endif //CONTROLCONTROLLER_H
