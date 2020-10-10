#! /usr/bin/env python
#-*-coding:utf-8-*-
import parseSettings as ps
import os
import speech_recognition as sr
import SoundPlayer as sp

def Google():
    
    r = sr.Recognizer()
    with sr.WavFile("voice.wav") as source:              # use "test.wav" as the audio source
       audio = r.record(source)                        # extract audio data from the file

    t = 0
    
    try:
        t = r.recognize_google(audio,language = "ru-RU", key = "AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw")
        print(t)   # recognize speech using Google Speech Recognition
    except LookupError:                                 # speech is unintelligible
        print("Could not understand audio")
        return False
    except sr.UnknownValueError:
        print("Google could not understand audio")
        return False
        #pass
    except sr.RequestError as e:
        print("Google error; {0}".format(e))
        return False
    if t  == 0:
        print("Error")
    elif t==("Привет") or t==("привет") or t==("Hi") or t==("Hello"): #Hi
        sp.Play_Hello()
    elif t==("Скажи время") or t==("Текущее время") or t==("Current time") or t==("Tell time"):
        sp.Play_Current_Time()
    elif t==("Как дела") or t==("How are you"):
        sp.Play_HRU()
    elif t==("Скажи настройки") or t==("Текущие настройки") or t==("Current settings"):
        sp.Play_Current_Settings()
    elif t.find("Привет") != -1 or t.find("привет") != -1 or t.find("Hi") != -1 or t.find("Hello") != -1: #Hi
        sp.Play_Hello()
    elif t.find("Время") != -1 or t.find("время") != -1:
        sp.Play_Current_Time()
    elif t.find("Как дела") != -1 or t.find("как дела") != -1 or t.find("How are you") != -1 or t.find("how are you") != -1:
        sp.Play_HRU()
    elif t.find("Настройки") != -1 or t.find("настройки") != -1 or t.find("Опции") != -1 or t.find("опции") != -1:
        sp.Play_Current_Settings()
    else:
        return False
     
    return True


def Spinx():
    
    r = sr.Recognizer()
    with sr.WavFile("voice.wav") as source:             
        audio = r.record(source)
        
    t = 0
    try:
        t = r.recognize_sphinx(audio)
        print(t) 
    except LookupError:                                
        print("Could not understand audio")
    except sr.UnknownValueError:
        print("Sphinx could not understand audio")
    except sr.RequestError as e:
        print("Sphinx error; {0}".format(e))
        
    if t==("hi"):
        sp.Play_Hello()
    elif t==("time"):
        sp.Play_Current_Time()
    elif t==("how are you"):
        sp.Play_HRU()
    elif t==("settings"):
        sp.Play_Current_Settings()
       
def Start_Record():
    #test purpose
    r = sr.Recognizer()
    with sr.WavFile("robo.wav") as source:              
        audio = r.record(source)        

    t = 0
    try:
        t = r.recognize_sphinx(audio)
        print(t)   
    except LookupError:                                
        print("Could not understand audio")
        return False
    except sr.UnknownValueError:
        print("Sphinx could not understand audio")
        return False
    except sr.RequestError as e:
        print("Sphinx error; {0}".format(e))
        return False
      

    if t.find("robo") != -1 or t.find("hi") != -1 or t.find("hello") != -1 or t.find("ok") != -1 or t.find("okay") != -1:
       return True

if __name__ == '__main__':
    Start_Record()
