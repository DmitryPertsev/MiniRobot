#include "ArduinoInteractionManager.h"

ArduinoInteractionManager::ArduinoInteractionManager()
{
}


ArduinoInteractionManager::~ArduinoInteractionManager()
{
  
}

void ArduinoInteractionManager::begin()
{
    serialcomm = new SerialIntercommunicator();
    movement = new MovementController();
}

void ArduinoInteractionManager::handle()
{
    serialcomm->run();
    movement->run();
}

MovementController ArduinoInteractionManager::getMovementController()
{
    return *movement;
}

void ArduinoInteractionManager::setMovementController(MovementController * movementController)
{
    movement = movementController;
}

SerialIntercommunicator ArduinoInteractionManager::getSerialIntercommunicator()
{
    return *serialcomm;
}

void ArduinoInteractionManager::setSerialIntercommunicator(SerialIntercommunicator * serialIntercomm)
{
    serialcomm = serialIntercomm;
}
