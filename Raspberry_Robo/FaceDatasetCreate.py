import cv2
import numpy
import os
import FacePreProcessing as fp
# Path for face image database
path = './data'

def Get_Paths():
    return [os.path.join(path,f) for f in sorted(os.listdir(path))]

def Get_Id(imagePath):
    return int(os.path.split(imagePath)[-1].split(".")[1])

def Get_Next_Id():
    imagePaths = [os.path.join(path,f) for f in sorted(os.listdir(path))]
    cur_id = 0
    id = 0
    for imagePath in imagePaths:
        print ('\n Path = ' + imagePath)
        cur_id = int(os.path.split(imagePath)[-1].split(".")[1])
        print('Subject id = ' + os.path.split(imagePath)[-1].split(".")[1])
        if cur_id == id +1:
            id = cur_id
        elif cur_id > (id + 1):
            return id + 1

            
    return id + 1
        
        
def Delete_Subject(num):
    imagePaths = [os.path.join(path,f) for f in sorted(os.listdir(path))]
    cur_id = 0
    id = 0
    i = 0
    for imagePath in imagePaths:
        cur_id = int(os.path.split(imagePath)[-1].split(".")[1])
        if cur_id == num:
            os.remove(imagePath)
    return

def Create_Dataset(sound_enable):
    print("\n Create face dataset ...")
    cam = cv2.VideoCapture(0)
    cam.set(3, 640) # set video width
    cam.set(4, 480) # set video height

    face_detector = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
    id = Get_Next_Id()

    if sound_enable == 1:
        os.system('mpg321 -a plughw:1,0 ./Sounds/blip.mp3')
        
    count = 0
    while(count < 100):

        ret, img = cam.read()
        img = cv2.flip(img, -1) # flip video image vertically
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        faces = face_detector.detectMultiScale(gray, 1.3, 5)
        for (x,y,w,h) in faces:
            
            resized_image = cv2.resize(gray[y:y+h,x:x+w],(350,350))
            resized_image = fp.PreProcessing_Origin(resized_image, 350, 350)
            path = "data/subject." + str(id) + '.' + str(count) + ".bmp"
            cv2.imwrite(path, resized_image)
            if count == 100:
                break;
            count += 1
            print(count)
        
        if sound_enable == 1:
            os.system('mpg321 -a plughw:1,0 ./Sounds/blip.mp3')

    cam.release()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    Create_Dataset()
    

