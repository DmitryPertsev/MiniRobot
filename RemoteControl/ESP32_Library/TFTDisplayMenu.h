//
// Created by Anzhalika Dziarkach on 04.04.2022.
//

#ifndef TFTDISPLAYMENU_H
#define TFTDISPLAYMENU_H

#include <SPI.h>
#include <TFT_eSPI.h>

#define BLUE_COLOR 0x07FF

class TFTDisplayMenu {
public:
    TFTDisplayMenu();
    ~TFTDisplayMenu();

    void clearDisplay();

    void showConnectionPage();

    void showInfoPage();
    void showInfoPageInformation();

    void showMovementPage();
    void showMovementPageInformation(int x, int y, bool z, String direction, int speed);

    void showCameraMovementPage();
    void showCameraMovementPageInformation(int x, int y, bool z, String cameraHorizontalDirection,
                                           String cameraVerticalDirection);

    void showVideoPage();

    void showSettingsPage();
    void showSettingsPageInformation(bool isSound, bool isVibro, int settingsPosition);

private:
    TFT_eSPI tft = TFT_eSPI();
};


#endif //TFTDISPLAYMENU_H
