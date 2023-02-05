//
// Created by Anzhalika Dziarkach on 05.04.2022.
//

#ifndef WIFI_CONTROLLER_H
#define WIFI_CONTROLLER_H

#include <WiFi.h>
#include <SPI.h>
#include "Arduino.h"
#include "pch.h"

const char *ROBOT_SSID = "Multitasking_robot";
const char *ROBOT_PASSWORD = "robot123";
const uint16_t ROBOT_PORT = 65432;
const char *ROBOT_IP = "192.168.1.23";

const String FORWARD_MOVEMENT = "FORWARD";
const String BACK_MOVEMENT = "BACK";
const String RIGHT_MOVEMENT = "RIGHT";
const String LEFT_MOVEMENT = "LEFT";
const String UP_MOVEMENT = "UP";
const String DOWN_MOVEMENT = "DOWN";

const String FORWARD_MOVEMENT_CODE = "f";
const String BACK_MOVEMENT_CODE = "b";
const String RIGHT_MOVEMENT_CODE = "r";
const String LEFT_MOVEMENT_CODE = "l";
const String STOP_MOVEMENT_CODE = "s";

const char* RIGHT_HORIZONTAL_CODE = "h85";
const char* LEFT_HORIZONTAL_CODE = "h5";
const char* UP_VERTICAL_CODE = "v40";
const char* DOWN_VERTICAL_CODE = "v5";

class WifiController {
public:
    WifiController();
    ~WifiController();

    void begin();

    char readByte();
    void send(char* information);

    void sendMovementInformation(String directionMovement, int speed);
    void sendCameraMovementInformation(String horizontalDirection, String verticalDirection);

    bool isWifiConnectionAvailable();
    bool isMessageToAccept();

private:
    WiFiClient client;
};


#endif //WIFI_CONTROLLER_H
