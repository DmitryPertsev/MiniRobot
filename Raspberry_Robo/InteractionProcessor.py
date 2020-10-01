import MovementProcessing
import FaceRecognitionProcessing
import os
import errno
import signal
import time
import cv2
import ParseSettings as ps

face_recognition = 1
subject_monitoring = 1
sound_enable = 1
movement_enable = 1

FIFO = 'tointeraction'

try:
     os.mkfifo(FIFO)
except OSError as oe: 
    if oe.errno != errno.EEXIST:
        raise
    
def runCommand(command):
    global face_recognition
    temp = face_recognition
    print(command)
    commandParam = command.split()
    print (commandParam)
    if command == "add":
        face_recognition = 0
        MovementProcessing.Initial_State()
        FaceRecognitionProcessing.Add_User()
        face_recognition = temp
    elif commandParam[0] == 'remove':
        face_recognition = 0
        MovementProcessing.Initial_State()
        FaceRecognitionProcessing.Delete_User(commandParam[1])
        face_recognition = temp
    elif commandParam[0] == 'facerec':
        
        face_recognition = int(commandParam[1])
    elif commandParam[0] == 'submon':
        global subject_monitoring
        subject_monitoring = int(commandParam[1])
    elif commandParam[0] == 'sound':
        global sound_enable
        sound_enable = int(commandParam[1])
    elif commandParam[0] == 'opmode':
        global movement_enable
        movement_enable = int(commandParam[1])
        if movement_enable == 0:
             print(' ')
             MovementProcessing.Initial_State()
        

def receiveSignal(signalNumber, frame):
    print('Received:', signalNumber)
    print("Opening FIFO...")
    with open(FIFO, "r" ) as fifo:
        print("FIFO opened")
        for line in fifo:
            runCommand(line)
    return

def loadSetting():
    face_recognition = int(ps.get_setting("settings.ini","Settings","Face recognition"))
    subject_monitoring = int(ps.get_setting("settings.ini","Settings","Subject monitoring"))
    sound_enable = int(ps.get_setting("settings.ini","Settings","Sound"))
    movement_enable = int(ps.get_setting("settings.ini","Settings","Operation mode"))

def Search():
    count = 0
    detected = 0
    result = False
    print ('face_recognition = ' + str(face_recognition))
    if face_recognition == 0:
        return result
    while (count < 30):
        found, image = FaceRecognitionProcessing.detect.Find_Face()
        if found :
            id, confidence = FaceRecognitionProcessing.recognizer.predict(image)
            if(confidence < 95):
                result = True
                print('see you')
                if(detected > 10):
                    if movement_enable == 1 and subject_monitoring == 1:
                        if sound_enable == 1:
                            os.system('mpg321 -a plughw:1,0 ./Sounds/blip.mp3')
                        MovementProcessing.Monitor_User(FaceRecognitionProcessing.detect.ix,
                                                        FaceRecognitionProcessing.detect.iy,
                                                        FaceRecognitionProcessing.detect.iw,
                                                        FaceRecognitionProcessing.detect.ih)
           
                    count = 0
                    detected = 0
                detected += 1
            else:
                count+=1
        else :
            count+=1
            print('no face')

    return result

ps.update_setting("settings.ini", "PIDS", "Interaction", str(os.getpid()))
signal.signal(signal.SIGUSR1, receiveSignal)
print('Signal handler set')
loadSetting()
print('Process settings loaded')
MovementProcessing.Initial_State()

while True:
    if not Search():
        print ('user not found')
        if movement_enable == 1:
            if sound_enable == 1:
                   os.system('mpg321 -a plughw:1,0 ./Sounds/blip2.mp3')
            MovementProcessing.Movement_Interaction()
            time.sleep(1)
        
    k = cv2.waitKey(100) & 0xff 
    if k == 27:
        break;

