import cv2
import FacePreProcessing as fp
# iniciate id counter
id = 0

# Initialize and start realtime video capture
cam = cv2.VideoCapture(0)
cam.set(3, 640)  # set video widht
cam.set(4, 480)  # set video height

# Define min window size to be recognized as a face
minW = 0.1 * cam.get(3)
minH = 0.1 * cam.get(4)
cascadePath = "haarcascade_frontalface_default.xml"
faceCascade = cv2.CascadeClassifier(cascadePath);
ix = 0
iy = 0
iw = 0
ih = 0

def Release_Cam():
    global cam
    cam.release()
    
def Reset_Cam():
    global cam
    cam = cv2.VideoCapture(0)
    cam.set(3, 640)  # set video widht
    cam.set(4, 480)  # set video height

def Find_Face():

    global ix
    global iy
    global iw
    global ih
    ret, img = cam.read()
    img = cv2.flip(img, -1) # flip video image vertically
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    found = False

    faces = faceCascade.detectMultiScale(
        gray,
        scaleFactor=1.2,
        minNeighbors=5,
        minSize=(int(minW), int(minH))
    )

    for (x, y, w, h) in faces:

        resized_image = cv2.resize(gray[y:y + h, x:x + w], (350, 350))
        resized_image = fp.PreProcessing_Origin(resized_image, 350, 350)
        ix = x
        iy = y
        iw = w
        ih = h

        return True, resized_image
    return False, 0
