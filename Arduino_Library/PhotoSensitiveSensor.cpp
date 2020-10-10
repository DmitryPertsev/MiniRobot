#include "PhotoSensitiveSensor.h"

PhotoSensitiveSensor::~PhotoSensitiveSensor()
{
}

PhotoSensitiveSensor::PhotoSensitiveSensor()
{
}

void PhotoSensitiveSensor::init(int sensorPin)
{
    mSensorPin = sensorPin;
    pinMode(sensorPin, INPUT);
}

int PhotoSensitiveSensor::read()
{
    state = analogRead(mSensorPin);
    return state;
}

float PhotoSensitiveSensor::normalizeData()
{
    float normal = (float)state / 512;
    return normal;
}