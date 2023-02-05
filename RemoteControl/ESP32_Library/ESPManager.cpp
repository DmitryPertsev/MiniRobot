//
// Created by Anzhalika Dziarkach on 03.04.2022.
//

#include "ESPManager.h"

ESPManager::ESPManager() {
    Serial.begin(115200);
    Serial.println("Start ESPManager");
}

ESPManager::~ESPManager() {

}

void ESPManager::begin() {
    controlController.turnOnSound();
    delay(500);
    controlController.turnOffSound();

    displayMenuController.showConnection();
    wifiController.begin();
    displayMenuController.showPage();
}

void ESPManager::handle() {
    if (!wifiController.isWifiConnectionAvailable()) {
        controlController.turnOffSound();
        controlController.turnOffVibro();
        begin();
    }

    int cameraX = controlController.getXPositionFromCameraJoystick();
    int cameraY = controlController.getYPositionFromCameraJoystick();
    bool cameraZ = controlController.isCameraButtonPressed();

    int movementX = controlController.getXPositionFromMovementJoystick();
    int movementY = controlController.getYPositionFromMovementJoystick();
    bool movementZ = controlController.isMovementButtonPressed();

    String movementDirection = controlController.getMovementDirection(movementX, movementY);
    int movementSpeed = controlController.getMovementSpeed(movementX, movementY);

    String cameraHorizontalDirection = controlController.getCameraHorizontalDirection(cameraX, cameraY);
    String cameraVerticalDirection = controlController.getCameraVerticalDirection(cameraX, cameraY);

    wifiController.sendMovementInformation(movementDirection, movementSpeed);
    wifiController.sendCameraMovementInformation(cameraHorizontalDirection, cameraVerticalDirection);

    if (wifiController.isMessageToAccept()) {
        char code = wifiController.readByte();
        if (code == 's') {
            if (displayMenuController.getSoundSetting()) {
                controlController.turnOnSound();
            } else {
                controlController.turnOffSound();
            }
            if (displayMenuController.getVibroSetting()) {
                controlController.turnOnVibro();
            } else {
                controlController.turnOffVibro();
            }
        } else {
            controlController.turnOffSound();
            controlController.turnOffVibro();
        }
    }

    displayMenuController.updateMenuInformation(movementX, movementY, movementZ, cameraX, cameraY, cameraZ,
                                                movementDirection,
                                                movementSpeed, cameraHorizontalDirection, cameraVerticalDirection);
    displayMenuController.execute();
}

