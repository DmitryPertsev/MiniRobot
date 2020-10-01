import FaceDatasetCreate as fd
import FaceRecognitionTrain as ft
import FaceDetection as detect
import FacePreProcessing as preproc
import cv2
import time
import os
import struct
import sys
import math

recognizer = cv2.face.LBPHFaceRecognizer_create()
recognizer.read('trainer/trainer.yml')

def Add_User(sound_enable = True):
    print('add user')
    detect.Release_Cam()
    fd.Create_Dataset(sound_enable)
    ft.Train_LBPH()
    detect.Reset_Cam()
    recognizer.read('trainer/trainer.yml')
    
def Delete_User(number):
    fd.Delete_Subject(number)
    ft.Train_LBPH()
    recognizer.read('trainer/trainer.yml')
