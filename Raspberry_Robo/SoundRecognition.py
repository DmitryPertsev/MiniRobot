import SoundAnalyzer as sa
import ParseSettings as ps
import subprocess
import os
import errno
import signal

FIFO = 'tosound'
sound_enable = 1
mic_enable = 1
microphone = 'plughw:2,0'
find = False

microphone_device_1 = 'plughw:2,0'
microphone_device_0 = 'plughw:1,0'

try:
     os.mkfifo(FIFO)
except OSError as oe: 
    if oe.errno != errno.EEXIST:
        raise
    
def runCommand(command):
    print(command)
    commandParam = command.split()
    if command[0:5] == "sound":
        global sound_enable
        sound_enable = int(commandParam[1])
    elif command[0:3] == "mic":
        global mic_enable
        mic_enable = int(commandParam[1])
    elif command[0:6] == "opmode":
        global microphone
        if int(commandParam[1]) == 0:
            microphone = microphone_device_0
        else:
            microphone = microphone_device_1
     elif command == "find":
         global find
         if not find:
             find = True
         else:
             find = False
            
    
def receiveSignal(signalNumber, frame):
    print('Received:', signalNumber)
    print("Opening FIFO...")
    with open(FIFO, "r" ) as fifo:
        print("FIFO opened")
        for line in fifo:
            runCommand(line)
    return

def loadSetting():
    mic_enable = int(ps.get_setting("settings.ini","Settings","Mic"))
    sound_enable = int(ps.get_setting("settings.ini","Settings","Sound"))
    if int(ps.get_setting("settings.ini","Settings","Operation mode")) == 0:
        microphone = microphone_device_0
    else:
        microphone = microphone_device_1


ps.update_setting("settings.ini", "PIDS", "Sound", str(os.getpid()))
signal.signal(signal.SIGUSR1, receiveSignal)
loadSetting()

#dictionary
#/home/<user>/.local/lib/python3.7/site-packages/speech_recognition/pocketsphinx-data/en-US
while True:

    if find:
        out = subprocess.Popen(['aplay','-D', microphone_device_0 ,'/home/Robo/Sounds/play.wav'], 
                stderr=subprocess.STDOUT,
                shell=False)
        out.wait()
        continue
     
    if sound_enable == 0 or mic_enable == 0:
        continue
       
    out = subprocess.Popen(['arecord','-f', 'dat', '-d', '4', '-D', microphone ,'voice.wav'], 
            stderr=subprocess.STDOUT,
                         shell=False)
    out.wait()
    if sa.Google() == False:
        print("Try sphinx\n")
        sa.Spinx()
    os.remove('voice.wav')
