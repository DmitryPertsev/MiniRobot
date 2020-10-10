#ifndef InteractionCommand_H
#define InteractionCommand_H

#include "Arduino.h"

class InteractionCommand
{
public:
    InteractionCommand();
    ~InteractionCommand();
    virtual void execute() = 0;
};

#endif
