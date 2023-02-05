//
// Created by Anzhalika Dziarkach on 04.04.2022.
//

#include "DisplayMenuController.h"

DisplayMenuController::DisplayMenuController() {
    currentPage = INFO_PAGE_POSITION;
    currentSettingPosition = SOUND_SETTING_POSITION;
    vibroSetting = true;
    soundSetting = true;

    nextPageButton.setDebounce(BUTTON_DEBOUNCE);
    nextSettingButton.setDebounce(BUTTON_DEBOUNCE);
    previousSettingButton.setDebounce(BUTTON_DEBOUNCE);
    changeSettingButton.setDebounce(BUTTON_DEBOUNCE);

    Serial.println("DisplayMenuController loaded");
}

DisplayMenuController::~DisplayMenuController() {

}

void DisplayMenuController::showConnection() {
    displayMenu.showConnectionPage();
}

void DisplayMenuController::execute() {
    nextPageButton.tick();
    if (nextPageButton.isPress()) {
        nextPage();
    }

    nextSettingButton.tick();
    if (nextSettingButton.isPress()) {
        nextSettingPosition();
    }

    previousSettingButton.tick();
    if (previousSettingButton.isPress()) {
        previousSettingPosition();
    }

    changeSettingButton.tick();
    if (changeSettingButton.isPress() && currentPage == SETTINGS_PAGE_POSITION) {
        changeSetting();
    }

    switch (currentPage) {
        case MOVEMENT_PAGE_POSITION:

            displayMenu.showMovementPageInformation(movementX, movementY, movementButton, movementDirection,
                                                    movementSpeed);
            break;

        case CAMERA_PAGE_POSITION:
            displayMenu.showCameraMovementPageInformation(cameraX, cameraY, cameraButton, cameraHorizontalDirection,
                                                          cameraVerticalDirection);
            break;

        case SETTINGS_PAGE_POSITION:
            displayMenu.showSettingsPageInformation(soundSetting, vibroSetting, currentSettingPosition);
            break;

        default:
            break;
    }
}

void DisplayMenuController::nextPage() {
    currentSettingPosition = SOUND_SETTING_POSITION;
    currentPage++;
    if (currentPage >= NUMBER_OF_PAGES) {
        currentPage = INFO_PAGE_POSITION;
    }

    showPage();
}

void DisplayMenuController::showPage() {
    displayMenu.clearDisplay();
    switch (currentPage) {
        case INFO_PAGE_POSITION:
            displayMenu.showInfoPage();
            break;

        case MOVEMENT_PAGE_POSITION:
            displayMenu.showMovementPage();
            displayMenu.showMovementPageInformation(movementX, movementY, movementButton, movementDirection,
                                                    movementSpeed);
            break;

        case CAMERA_PAGE_POSITION:
            displayMenu.showCameraMovementPage();
            displayMenu.showCameraMovementPageInformation(cameraX, cameraY, cameraButton, cameraHorizontalDirection,
                                                          cameraVerticalDirection);
            break;

        case VIDEO_PAGE_POSITION:
            displayMenu.showVideoPage();
            break;

        case SETTINGS_PAGE_POSITION:
            displayMenu.showSettingsPage();
            displayMenu.showSettingsPageInformation(soundSetting, vibroSetting, currentSettingPosition);
            break;

        default:
            break;
    }
}

void DisplayMenuController::nextSettingPosition() {
    if (currentSettingPosition < NUMBER_OF_SETTINGS - 1) {
        currentSettingPosition++;
    }
    if (currentPage == SETTINGS_PAGE_POSITION) {
        displayMenu.showSettingsPageInformation(soundSetting, vibroSetting, currentSettingPosition);
    }
}

void DisplayMenuController::previousSettingPosition() {
    if (currentSettingPosition > SOUND_SETTING_POSITION) {
        currentSettingPosition--;
    }
    if (currentPage == SETTINGS_PAGE_POSITION) {
        displayMenu.clearDisplay();
        displayMenu.showSettingsPage();
        displayMenu.showSettingsPageInformation(soundSetting, vibroSetting, currentSettingPosition);
    }
}

void DisplayMenuController::changeSetting() {
    switch (currentSettingPosition) {
        case SOUND_SETTING_POSITION:
            soundSetting = !soundSetting;
            break;
        case VIBRO_SETTING_POSITION:
            vibroSetting = !vibroSetting;
            break;
        default:
            break;
    }

    displayMenu.clearDisplay();
    displayMenu.showSettingsPage();
    displayMenu.showSettingsPageInformation(soundSetting, vibroSetting, currentSettingPosition);
}

bool DisplayMenuController::getSoundSetting() {
    return soundSetting;
}

bool DisplayMenuController::getVibroSetting() {
    return vibroSetting;
}

void DisplayMenuController::updateMenuInformation(int movementX, int movementY, bool movementButton, int cameraX,
                                                  int cameraY, bool cameraButton, String movementDirection,
                                                  int movementSpeed, String cameraHorizontalDirection,
                                                  String cameraVerticalDirection) {
    this->movementX = movementX;
    this->movementY = movementY;
    this->movementButton = movementButton;
    this->cameraX = cameraX;
    this->cameraY = cameraY;
    this->cameraButton = cameraButton;
    this->movementDirection = movementDirection;
    this->movementSpeed = movementSpeed;
    this->cameraHorizontalDirection = cameraHorizontalDirection;
    this->cameraVerticalDirection = cameraVerticalDirection;
}