import os
import datetime
import parseSettings as ps

HRU = ["Все хорошо", "Пора бы отдохнуть","А у тебя как?"]

def Play_Current_Time():
    currentDT = datetime.datetime.now()
    time = 'espeak-ng --stdout -vru+f5 -p 50 -g 10ms -s 160 "Текущее время ' + str(currentDT.hour)  + ' часов и ' + str(currentDT.minute) + ' минут" | aplay -D plughw:1,0' 
    os.system(time)

def Play_Current_Settings():
    settings = 'espeak-ng --stdout -vru+f5 -p 50 -g 10ms -s 160 "Текущие настройки ' + ps.get_setting_str() + ' " | aplay -D plughw:1,0' 
    pid = os.system(settings)
    
def Play_Hello():
    os.system('espeak-ng --stdout -vru+f5 -p 80 -g 20ms -s 160 "Привет" | aplay -D plughw:1,0')

def Play_HRU():
    num = random.randint(0,2)
    phrase = 'espeak-ng --stdout -vru+f5 -p 50 -g 10ms -s 160 "' + HRU[num] + '" | aplay -D plughw:1,0' 
