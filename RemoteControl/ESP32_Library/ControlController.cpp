//
// Created by Anzhalika Dziarkach on 03.04.2022.
//

#include "ControlController.h"

ControlController::ControlController() {
    pinMode(LED_PIN, OUTPUT);
    pinMode(VIBRO_PIN, OUTPUT);
    pinMode(SOUND_PIN, OUTPUT);

    pinMode(MOVEMENT_JOYSTICK_X, INPUT);
    pinMode(MOVEMENT_JOYSTICK_Y, INPUT);
    pinMode(MOVEMENT_JOYSTICK_Z, INPUT);

    pinMode(CAMERA_JOYSTICK_X, INPUT);
    pinMode(CAMERA_JOYSTICK_Y, INPUT);
    pinMode(CAMERA_JOYSTICK_Z, INPUT);

    Serial.println("ControlController loaded");
}

ControlController::~ControlController() {

}

int ControlController::getXPositionFromMovementJoystick() {
    return analogRead(MOVEMENT_JOYSTICK_X);
}

int ControlController::getYPositionFromMovementJoystick() {
    return analogRead(MOVEMENT_JOYSTICK_Y);
}

bool ControlController::isMovementButtonPressed() {
    return digitalRead(MOVEMENT_JOYSTICK_Z);
}

int ControlController::getXPositionFromCameraJoystick() {
    return analogRead(CAMERA_JOYSTICK_X);
}

int ControlController::getYPositionFromCameraJoystick() {
    return analogRead(CAMERA_JOYSTICK_Y);
}

bool ControlController::isCameraButtonPressed() {
    return digitalRead(CAMERA_JOYSTICK_Z);
}

void ControlController::turnOnLED() {
    digitalWrite(LED_PIN, HIGH);
}

void ControlController::turnOffLED() {
    digitalWrite(LED_PIN, LOW);
}

void ControlController::turnOnSound() {
    digitalWrite(SOUND_PIN, HIGH);
}

void ControlController::turnOffSound() {
    digitalWrite(SOUND_PIN, LOW);
}

void ControlController::turnOnVibro() {
    digitalWrite(VIBRO_PIN, HIGH);
}

void ControlController::turnOffVibro() {
    digitalWrite(VIBRO_PIN, LOW);
}

String ControlController::getMovementDirection(int x, int y) {
    String result = STOP_MOVEMENT;

    if (y > BASE_AXIS_VALUE + AXIS_INFELICITY) {
        result = FORWARD_MOVEMENT;
    } else if (y < BASE_AXIS_VALUE - AXIS_INFELICITY) {
        result = BACK_MOVEMENT;
    } else if (x > BASE_AXIS_VALUE + AXIS_INFELICITY) {
        result = RIGHT_MOVEMENT;
    } else if (x < BASE_AXIS_VALUE - AXIS_INFELICITY) {
        result = LEFT_MOVEMENT;
    }

    return result;
}

int ControlController::getMovementSpeed(int x, int y) {
    String directionMovement = getMovementDirection(x, y);
    int result = 0;
    if (directionMovement == BACK_MOVEMENT) {
        result = (MIN_AXIS_VALUE - y) * 100 / MIN_AXIS_VALUE;
    } else if (directionMovement == FORWARD_MOVEMENT) {
        result = (y - BASE_AXIS_VALUE - AXIS_INFELICITY) * 100 /
                 (MAX_AXIS_VALUE - BASE_AXIS_VALUE - AXIS_INFELICITY);
    } else if (directionMovement == LEFT_MOVEMENT) {
        result = (MIN_AXIS_VALUE - x) * 100 / MIN_AXIS_VALUE;
    } else if (directionMovement == RIGHT_MOVEMENT) {
        result = (x - BASE_AXIS_VALUE - AXIS_INFELICITY) * 100 /
                 (MAX_AXIS_VALUE - BASE_AXIS_VALUE - AXIS_INFELICITY);
    }
    return result;
}

String ControlController::getCameraHorizontalDirection(int x, int y) {
    String result = STOP_MOVEMENT;

    if (x > BASE_AXIS_VALUE + AXIS_INFELICITY) {
        result = RIGHT_MOVEMENT;
    } else if (x < BASE_AXIS_VALUE - AXIS_INFELICITY) {
        result = LEFT_MOVEMENT;
    }

    return result;
}

String ControlController::getCameraVerticalDirection(int x, int y) {
    String result = STOP_MOVEMENT;

    if (y > BASE_AXIS_VALUE + AXIS_INFELICITY) {
        result = UP_MOVEMENT;
    } else if (y < BASE_AXIS_VALUE - AXIS_INFELICITY) {
        result = DOWN_MOVEMENT;
    }

    return result;
}