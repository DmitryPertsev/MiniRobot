#ifndef SaveStateSensor_H
#define SaveStateSensor_H

#include "Arduino.h"

class SaveStateSensor
{
public:
    SaveStateSensor();
    ~SaveStateSensor();

    virtual int read() = 0;
    int getCurrentState();

protected:
    int state;
};

#endif