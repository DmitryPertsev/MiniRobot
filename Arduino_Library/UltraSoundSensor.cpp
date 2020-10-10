#include "UltraSoundSensor.h"

UltraSoundSensor::UltraSoundSensor() : mDuration(0)
{
    state = 3000;
}

UltraSoundSensor::UltraSoundSensor(int trigPin, int echoPin) : mDuration(0)
{
    mTrigPin = trigPin;
    mEchoPin = echoPin;

    pinMode(mTrigPin, OUTPUT);
    pinMode(mEchoPin, INPUT);
}

UltraSoundSensor::~UltraSoundSensor() 
{
}

void UltraSoundSensor::init(int trigPin, int echoPin)
{
    mTrigPin = trigPin;
    mEchoPin = echoPin;

    pinMode(mTrigPin, OUTPUT);
    pinMode(mEchoPin, INPUT);
}

int UltraSoundSensor::read() 
{
    digitalWrite(mTrigPin, LOW);
    delayMicroseconds(2);
    // Sets the trigPin HIGH (ACTIVE) for 10 microseconds
    digitalWrite(mTrigPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(mTrigPin, LOW);
    // Reads the echoPin, returns the sound wave travel time in microseconds
    mDuration = pulseIn(mEchoPin, HIGH);
    // Calculating the distance
    state = mDuration * 0.034 / 2; // Speed of sound wave divided by 2 (go and back)
    return state;
}
