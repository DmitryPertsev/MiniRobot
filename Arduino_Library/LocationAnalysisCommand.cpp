#include "LocationAnalysisCommand.h"

LocationAnalysisCommand::LocationAnalysisCommand()
{
    mLocationState = "";
}

LocationAnalysisCommand::~LocationAnalysisCommand() 
{
}

void LocationAnalysisCommand::execute()
{
    IRSensor.read();
    int * irSensorsState = IRSensor.getResultAsArray();
    int distance = USSensor.read();
    int lightLevel = PSSensor.read();
    mLocationState = "";
    mLocationState += "i:";
    mLocationState += irSensorsState[0];
    mLocationState += ",";
    mLocationState += irSensorsState[1];
    mLocationState += ",";
    mLocationState += irSensorsState[2];
    mLocationState += ",";
    mLocationState += irSensorsState[3];
    mLocationState += "u:";
    mLocationState += distance;
    mLocationState += "l:";
    mLocationState += lightLevel;
    mLocationState += "\0";
};

const char * LocationAnalysisCommand::getResultString()
{
    return mLocationState.c_str();
}

LocationAnalysisCommand * LocationAnalysisCommand::init()
{
    USSensor.init(trigPin, echoPin);
    PSSensor.init(lightSensorPin);
    IRSensor.init(ikCenter, ikLeft, ikRight, ikBack);
}
