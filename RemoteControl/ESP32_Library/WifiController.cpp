//
// Created by Anzhalika Dziarkach on 05.04.2022.
//

#include "WifiController.h"

WifiController::WifiController() {
    pinMode(LED_PIN, OUTPUT);

    Serial.println("WifiController loaded");
}

WifiController::~WifiController() {

}

void WifiController::begin() {
    WiFi.begin(ROBOT_SSID, ROBOT_PASSWORD);
    while (WiFi.status() != WL_CONNECTED) {
        Serial.println("Connection to the robot ...");
        digitalWrite(LED_PIN, HIGH);
        delay(500);
        digitalWrite(LED_PIN, LOW);
        delay(500);
    }

    while (true) {
        if (!client.connect(ROBOT_IP, ROBOT_PORT)) {
            Serial.println("Connection to host failed");
            delay(1000);
        } else {
            Serial.println("Connected to server successful!");
            break;
        }
        digitalWrite(LED_PIN, HIGH);
        delay(500);
        digitalWrite(LED_PIN, LOW);
        delay(500);
    }


    Serial.println("Connected to the robot successfully");
    digitalWrite(LED_PIN, HIGH);
}

char WifiController::readByte() {
    char byte = ' ';
    if (isMessageToAccept()) {
        byte = client.read();
    }
    return byte;
}

void WifiController::send(char *information) {
    client.write(information);
}

void WifiController::sendMovementInformation(String directionMovement, int speed) {
    String result = STOP_MOVEMENT_CODE;

    if (directionMovement == FORWARD_MOVEMENT) {
        result = FORWARD_MOVEMENT_CODE + String(speed);
    } else if (directionMovement == BACK_MOVEMENT) {
        result = BACK_MOVEMENT_CODE + String(speed);
    } else if (directionMovement == LEFT_MOVEMENT) {
        result = LEFT_MOVEMENT_CODE + String(speed);
    } else if (directionMovement == RIGHT_MOVEMENT) {
        result = RIGHT_MOVEMENT_CODE + String(speed);
    }

    int str_len = result.length() + 1;
    char rs[str_len];
    result.toCharArray(rs, str_len);

    send(rs);
}

void WifiController::sendCameraMovementInformation(String horizontalDirection, String verticalDirection) {
    if (horizontalDirection == LEFT_MOVEMENT) {
        send((char *) LEFT_HORIZONTAL_CODE);
    } else if (horizontalDirection == RIGHT_MOVEMENT) {
        send((char *)  RIGHT_HORIZONTAL_CODE);
    }

    if (verticalDirection == UP_MOVEMENT) {
        send((char *) UP_VERTICAL_CODE);
    } else if (verticalDirection == DOWN_MOVEMENT) {
        send((char *) DOWN_VERTICAL_CODE);
    }
}

bool WifiController::isWifiConnectionAvailable() {
    return client.connected();
}

bool WifiController::isMessageToAccept() {
    return client.available() > 0;
}
