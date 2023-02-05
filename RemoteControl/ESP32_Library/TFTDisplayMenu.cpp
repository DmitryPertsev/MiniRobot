//
// Created by Anzhalika Dziarkach on 04.04.2022.
//

#include "TFTDisplayMenu.h"

TFTDisplayMenu::TFTDisplayMenu() {
    tft.init();
    tft.setRotation(3);
    clearDisplay();

    Serial.println("TFTDisplayMenu loaded");
}

TFTDisplayMenu::~TFTDisplayMenu() {

}

void TFTDisplayMenu::clearDisplay() {
    tft.fillScreen(TFT_BLACK);
    tft.setCursor(0, 0, 2);
    tft.setTextColor(TFT_WHITE, TFT_BLACK);
}

void TFTDisplayMenu::showConnectionPage() {
    clearDisplay();
    tft.setTextColor(TFT_YELLOW, TFT_BLACK);
    tft.println("       CONNECTION\n");

    tft.setTextColor(TFT_WHITE, TFT_BLACK);
    tft.println("Connection to the robot");
    tft.println("            ...");
}

void TFTDisplayMenu::showInfoPage() {
    tft.setTextColor(TFT_YELLOW, TFT_BLACK);
    tft.println("       INFORMATION\n");
}

void TFTDisplayMenu::showMovementPage() {
    tft.setTextColor(TFT_YELLOW, TFT_BLACK);
    tft.println("         MOVEMENT");

    tft.setTextColor(TFT_GREEN, TFT_BLACK);
    tft.println("x: ");
    tft.println("y: ");
    tft.println("z: ");

    tft.setTextColor(BLUE_COLOR, TFT_BLACK);
    tft.println("==========================");
    tft.println("direction: ");
    tft.println("speed: ");
}

void TFTDisplayMenu::showMovementPageInformation(int x, int y, bool z, String direction, int speed) {
    tft.setTextColor(TFT_WHITE, TFT_BLACK);
    tft.setCursor(20, 16, 2);
    tft.print(x);
    tft.print("     ");

    tft.setCursor(20, 32, 2);
    tft.print(y);
    tft.print("     ");

    tft.setCursor(20, 48, 2);
    tft.print(z);
    tft.print("     ");

    tft.setCursor(64, 80, 2);
    tft.print(direction);
    tft.print("     ");

    tft.setCursor(50, 96, 2);
    tft.print("       ");

    tft.setCursor(50, 96, 2);
    tft.print(speed);
    tft.print(" %");
}

void TFTDisplayMenu::showCameraMovementPage() {
    tft.setTextColor(TFT_YELLOW, TFT_BLACK);
    tft.println("   CAMERA MOVEMENT");

    tft.setTextColor(TFT_GREEN, TFT_BLACK);
    tft.println("x: ");
    tft.println("y: ");
    tft.println("z: ");

    tft.setTextColor(BLUE_COLOR, TFT_BLACK);
    tft.println("==========================");
    tft.println("horizontal: ");
    tft.println("vertical: ");
}

void TFTDisplayMenu::showCameraMovementPageInformation(int x, int y, bool z, String cameraHorizontalDirection,
                                                       String cameraVerticalDirection) {
    tft.setTextColor(TFT_WHITE, TFT_BLACK);
    tft.setCursor(20, 16, 2);
    tft.print(x);
    tft.print("     ");

    tft.setCursor(20, 32, 2);
    tft.print(y);
    tft.print("     ");

    tft.setCursor(20, 48, 2);
    tft.print(z);
    tft.print("     ");

    tft.setCursor(74, 80, 2);
    tft.print(cameraHorizontalDirection);
    tft.print("     ");

    tft.setCursor(60, 96, 2);
    tft.print(cameraVerticalDirection);
    tft.print("     ");
}

void TFTDisplayMenu::showVideoPage() {
    tft.setTextColor(TFT_YELLOW, TFT_BLACK);
    tft.println("          VIDEO\n");
}

void TFTDisplayMenu::showSettingsPage() {
    tft.setTextColor(TFT_YELLOW, TFT_BLACK);
    tft.println("         SETTINGS\n");

    tft.setTextColor(TFT_GREEN, TFT_BLACK);
    tft.println("sound: ");
    tft.println("vibro: ");
}

void TFTDisplayMenu::showSettingsPageInformation(bool isSound, bool isVibro, int settingsPosition) {
    tft.setTextColor(TFT_WHITE, TFT_BLACK);
    tft.setCursor(42, 32, 2);

    switch (settingsPosition) {
        case 0:
            tft.setTextColor(TFT_BLACK, TFT_WHITE);
            if (isSound) {
                tft.println("on ");
            } else {
                tft.println("off");
            }

            tft.setTextColor(TFT_WHITE, TFT_BLACK);
            tft.setCursor(42, 48, 2);
            if (isVibro) {
                tft.println("on ");
            } else {
                tft.println("off");
            }

            break;

        case 1:
            tft.setTextColor(TFT_WHITE, TFT_BLACK);
            if (isSound) {
                tft.println("on ");
            } else {
                tft.println("off");
            }

            tft.setTextColor(TFT_BLACK, TFT_WHITE);
            tft.setCursor(42, 48, 2);
            if (isVibro) {
                tft.println("on ");
            } else {
                tft.println("off");
            }

            break;
        default:
            tft.setTextColor(TFT_WHITE, TFT_BLACK);
            if (isSound) {
                tft.println("on ");
            } else {
                tft.println("off");
            }

            tft.setCursor(42, 48, 2);
            if (isVibro) {
                tft.println("on ");
            } else {
                tft.println("off");
            }
            break;
    }
}