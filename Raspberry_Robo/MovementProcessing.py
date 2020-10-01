import time
import serial
import os
import struct
import sys
import math
import SerialIntercomm as si

head_turns = 0
prev_direction = 0

def Initial_State():
    print('initial state')
    global head_turns
    global prev_direction
    si.Send_Command_Vertical_Servo(0)
    si.Send_Command_Horizontal_Servo(40)
    si.Send_Command_Stop()
    head_turns = 0
    prev_direction = 0
    return

def Monitor_User(x, y, w, h):
    print(':',x,y,w,h)
    Find_User(-1)
    if((x + w/2 < 300) | (x+ w/2 > 340)):
        distX = ((x + w/2) * 85) / 640
        si.Send_Command_Horizontal_Servo(distX)
        print("h" + str(distX) + "\n")

    if((y + h/2 < 200) | (y + h/2 > 280)):
        distY = ((y + h/2) * 40) / 480
        si.Send_Command_Vertical_Servo(distY)
        print("v" + str(distY) + "\n")


def Find_User(direction):

    if (direction == -1):
        si.Send_Command_Stop()
    elif(direction == 0):
        si.Send_Command_Forward()     
    elif(direction == 1):
        si.Send_Command_Left()
    elif(direction == 2):
        si.Send_Command_Right()
    else:
        si.Send_Command_Back()


def Turn_Around_Head(turn):

    if(turn == 0):
        si.Send_Command_Turn_Up()
    elif(turn == 1):
        si.Send_Command_Turn_Down()
    elif (turn == 2):
        si.Send_Command_Turn_Left()
    else:
        si.Send_Command_Turn_Right()


#prev_direction = 0 - Forward , 1 - Left, 2 - Right, 3 - back
def Movement_Interaction():
    global head_turns
    global prev_direction
    # load status arduino
    if(head_turns < 4):
        Turn_Around_Head(head_turns)
        head_turns+=1
    else:
        Find_User(prev_direction)
        if(prev_direction == 3):
            prev_direction = 0
        else:
            prev_direction+=1
        head_turns = 0
    time.sleep(1)
    
#exitflag = False


