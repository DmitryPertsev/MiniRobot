#include "MovementController.h"
#include "SerialIntercommunicator.h"

class ArduinoInteractionManager
{
public:
    ArduinoInteractionManager();
    ~ArduinoInteractionManager();

    void begin();
    void handle();
    MovementController getMovementController();
    void setMovementController(MovementController * movementController);
    SerialIntercommunicator getSerialIntercommunicator();
    void setSerialIntercommunicator(SerialIntercommunicator * serialIntercomm);

private:
    MovementController * movement;
    SerialIntercommunicator * serialcomm;
};
