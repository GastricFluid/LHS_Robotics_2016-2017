import cv2
import numpy as np
import CameraConfig
import math
import picamera
from picamera import PiCamera
from picamera.array import PiRGBArray

focal = 0
thresh = 0
videoSource=0
video=None
rawCapture = None

cols = 320
rows = 120
usb = True
test = 0

minLineLength = 0
maxLineGap = 0

lower = np.array([0,0,0], dtype = np.uint8)
upper = np.array([255, 255, 255], dtype = np.uint8)

imageType = 'HSV'


def calibrateCamera(knownDistance):
    lines=getLines()

    calculatedPixels = lineLength(midpt(lines[0][0]),midpt(lines[2][0]))

    focal= determineFocal(knownDistance, 2, calculatedPixels)
    #Store focal in file
    CameraConfig.write([127, 50, 5, focal, 320, 120, True], 'Camera.cfg')

def calibratePicture(test, imageType): #test is 0 if taking real image, 1 if taking test image
    CameraConfig.write([test, imageType], 'ImageType.cfg')
    
def calibrateFilter():
    #storing low and high values for the mask
    CameraConfig.write([88, 177, 220], 'LowerRopeFilterRGB.cfg')
    CameraConfig.write([212, 235, 255], 'UpperRopeFilterRGB.cfg')
    CameraConfig.write([0, 40, 164], 'LowerRopeFilterHSV.cfg')
    CameraConfig.write([38, 255, 255], 'UpperRopeFilterHSV.cfg')
    CameraConfig.write([0, 0, 0],'LowerTargetFilterRGB.cfg')
    CameraConfig.write([175, 158, 255], 'UpperTargetFilterRGB.cfg')
    CameraConfig.write([50, 0, 200], 'LowerTargetFilterHSV.cfg')
    CameraConfig.write([90, 255, 255], 'UpperTargetFilterHSV.cfg')

def init():
    #Read focal point from file
    global thresh, minLineLength, maxLineGap, focal, lower, upper, test, imageType, cols, rows, usb, rawCapture, video

    thresh, minLineLength, maxLineGap, focal, cols, rows, usb = CameraConfig.read('Camera.cfg')
    test, imageType = CameraConfig.read('ImageType.cfg')
    
    if imageType == 'RGB':
        rL, gL, bL = CameraConfig.read('LowerTargetFilterRGB.cfg')
        rU, gU, bU = CameraConfig.read('UpperTargetFilterRGB.cfg')
        lower = np.array([rL, gL, bL], dtype = np.uint8)
        upper = np.array([rU, gU, bU], dtype = np.uint8)
    elif imageType == 'HSV':
        hL, sL, vL = CameraConfig.read('LowerTargetFilterHSV.cfg')
        hU, sU, vU = CameraConfig.read('UpperTargetFilterHSV.cfg')
        lower = np.array([hL, sL, vL], dtype = np.uint8)
        upper = np.array([hU, sU, vU], dtype = np.uint8)

    if usb == True:
        video = PiCamera()
        video.resolution = (cols,rows)
        video.framerate = 24
        rawCapture = PiRGBArray(video,size=(cols,rows))
    else:
         video = cv2.VideoCapture(videoSource)
    return

def run():
    return

def view():
    rval = True

    while rval:
        cv2.imshow("preview", frame)
        rval, frame = video.read()
        key = cv2.waitKey(20)
        if key == 27: # exit on ESC
            break
    cv2.destroyWindow("preview")
    return

def grabPicture():
    if video is None:
        return None
    
    if usb == True:
        # grab an image from the camera
        video.capture(rawCapture, format="bgr")
        frame = rawCapture.array
        M = cv2.getRotationMatrix2D((cols/2,rows/2),-90,1)
        frame = cv2.warpAffine(frame,M,(cols,rows))
        return frame
    else:
        rval = 0
        if video.isOpened(): # try to get the first frame
            rval, frame = video.read()
        else:
            return None
        if rval == 1:
            return frame
    
        return None
    return None

def grabTestPicture():
    return cv2.imread(os.path.join(os.path.dirname(os.path.realpath(__file__)), 'test1.jpg'))

def getLines():
    if test == 0:
        img=grabPicture()
        if imageType == 'HSV':
            img = cv2.cvtColor(img,cv2.COLOR_BGR2HSV)
        mask = cv2.inRange(img, lower, upper)
        img = cv2.bitwise_not(mask)

    elif test == 1:
        img=grabTestPicture()
    if img is None:
        return None
        
    edges = cv2.Canny(img,50,120)

    lines = cv2.HoughLinesP(edges, 1, np.pi/180, thresh, minLineLength, maxLineGap)

    if lines is None:
        return None

    if (len(lines) == 1 and len(lines[0]) < 4) or (len(lines) != 1 and len(lines) < 4):
        return None
        
    ##cv2.destroyAllWindows()
    return lines

def findRope():
    img=grabPicture()
    if imageType == 'RGB':
        rL, gL, bL = CameraConfig.read('LowerRopeFilterRGB.cfg')
        rU, gU, bU = CameraConfig.read('UpperRopeFilterRGB.cfg')
        lower = np.array([rL, gL, bL], dtype = np.uint8)
        upper = np.array([rU, gU, bU], dtype = np.uint8)
    elif imageType == 'HSV':
        hL, sL, vL = CameraConfig.read('LowerRopeFilterHSV.cfg')
        hU, sU, vU = CameraConfig.read('UpperRopeFilterHSV.cfg')
        lower = np.array([hL, sL, vL], dtype = np.uint8)
        upper = np.array([hU, sU, vU], dtype = np.uint8)
        img = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    
    if img is None:
        return None
    
    mask = cv2.inRange(img, lower, upper)
    img = cv2.bitwise_not(mask)
    
    edges = cv2.Canny(img, 50, 120)
    lines = cv2.HoughLinesP(edges, 1, np.pi/180, thresh, minLineLength, maxLineGap)

    if lines is None:
        return None
    
    if (len(lines) == 1 and len(lines[0]) < 2) or (len(lines) != 1 and len(lines) < 2):
        return None
        
    ##cv2.destroyAllWindows()
    return lines

def lineLength(p1,p2):
    return math.sqrt((p2[0]-p1[0])**2+(p2[1]-p1[1])**2)

def midpt(line):
    midx = (line[0] + line[2])/2
    midy = (line[1] + line[3])/2
    return midx,midy

##This function determines the Focal Length of the camera
def determineFocal(knownDistance, knownWidth, calculatedLength):
    return calculatedLength * knownDistance / knownWidth

##This function calculates the straight line distance the camera is from
##a known object
def calculateDistance(focal, width, calculatedLength):
    return focal * width / calculatedLength

