import time
import serial
import os
import struct
import sys
import math

ser = serial.Serial(
    port='/dev/ttyACM0',
    baudrate=250000,
    parity=serial.PARITY_NONE,
    stopbits=serial.STOPBITS_ONE,
    bytesize=serial.EIGHTBITS
)

def Send_Command_Stop():
    ser.write(str.encode("s" + "\n"))

def Send_Command_Forward():
    print('finding you forward...')
    ser.write(str.encode("f" + "\n"))
    ser.write(str.encode("s" + "\n"))
    
def Send_Command_Left():
    print('finding you left...')
    ser.write(str.encode("l1000" + "\n"))
    ser.write(str.encode("f" + "\n"))
    ser.write(str.encode("s" + "\n"))
    
def Send_Command_Right():
    print('finding you right...')
    ser.write(str.encode("r1000" + "\n"))
    ser.write(str.encode("f" + "\n"))
    ser.write(str.encode("s" + "\n"))
    
def Send_Command_Back():
    print('finding you back...')
    ser.write(str.encode("b" + "\n"))
    ser.write(str.encode("s" + "\n"))
    
def Send_Command_Turn_Up():
    print('turn up...')
    ser.write(str.encode("v5" + "\n"))
    
def Send_Command_Turn_Down():
    print('turn down...')
    ser.write(str.encode("v40" + "\n"))
    
def Send_Command_Turn_Left():
    print('turn left...')
    ser.write(str.encode("h5" + "\n"))
    
def Send_Command_Turn_Right():
    print('turn right...')
    ser.write(str.encode("h85" + "\n"))

def Send_Command_Horizontal_Servo(distX):
    ser.write(str.encode("h" + str(distX) + "\n"))
    
def Send_Command_Vertical_Servo(distY):
    ser.write(str.encode("v" + str(distY) + "\n"))
    
def Send_Command_Forward_Time(timeDeltaSmall):
    ser.write("f" + "\n")
    time.sleep(timeDeltaSmall)
    ser.write("s" + "\n")
    time.sleep(1)
    
def Send_Command_Back_Time(timeDeltaBig):
    ser.write("b" + "\n")
    time.sleep(timeDeltaBig)
    ser.write("s" + "\n")
    time.sleep(1)
     
