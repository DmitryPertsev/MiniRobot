#include "SaveStateSensor.h"

SaveStateSensor::SaveStateSensor()
{
    state = 0;
}

SaveStateSensor::~SaveStateSensor()
{
}

int SaveStateSensor::getCurrentState()
{
    return state;
}
