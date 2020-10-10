#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
#include <AFMotor.h>
#include <ArduinoInteractionManager.h>

ArduinoInteractionManager manager = ArduinoInteractionManager();

void setup() 
{
  manager.begin();
}

void loop() 
{
  manager.handle();
}
