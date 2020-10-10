#ifndef LocationAnalysisCommand_H
#define LocationAnalysisCommand_H
#include "Arduino.h"
#include "InteractionCommand.h"
#include "UltraSoundSensor.h"
#include "InfraSensor.h"
#include "PhotoSensitiveSensor.h"

#define ikCenter 14
#define ikLeft 15
#define ikRight 16
#define ikBack 17 

#define trigPin 10
#define echoPin 9

#define lightSensorPin 18

class LocationAnalysisCommand : public InteractionCommand
{
public:
    LocationAnalysisCommand();
    ~LocationAnalysisCommand();

    void execute();
    const char * getResultString();
    LocationAnalysisCommand * init();

private:
    String mLocationState;
    UltraSoundSensor USSensor;
    PhotoSensitiveSensor PSSensor;
    InfraSensor IRSensor;
};

#endif
