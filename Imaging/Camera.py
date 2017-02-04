import cv2
import numpy as np
import calibrateCamera
import CameraConfig
import math

focal = 0
thresh = 0
videoSource=0
video=cv2.VideoCapture(videoSource)

minLineLength = 0
maxLineGap = 0

lower = np.array([0,0,0], dtype = np.uint8)
upper = np.array([255, 255, 255], dtype = np.uint8)

imageType = 'RGB'#CameraConfig.RGBorHSV()
#print imageType

def calibrateCamera(knownDistance):
    lines=getLines()

    calculatedPixels = lineLength(midpt(lines[0][0]),midpt(lines[2][0]))

    focal= calibrateCamera.determineFocal(knownDistance, 2, calculatedPixels)
    #Store focal in file
    CameraConfig.write([127, 50, 5, focal], 'Camera.cfg')

def calibratePicture(test, imageType):
    CameraConfig.write([test, imageType], 'ImageType.cfg')
    
def calibrateFilter():
    #storing low and high values for the mask
    CameraConfig.write([88, 212, 177, 235, 220, 255], 'RopeFilter.cfg')
    CameraConfig.write([0, 0, 0],'LowerTargetFilterRGB.cfg')
    CameraConfig.write([175, 158, 255], 'UpperTargetFilterRGB.cfg')
    CameraConfig.write([0, 0, 134], 'LowerTargetFilterHSV.cfg')
    CameraConfig.write([127, 100, 200], 'UpperTargetFilterHSV.cfg')

def init():
    #Read focal point from file
    global thresh, minLineLength, maxLineGap, focal, lower, upper

    thresh, minLineLength, maxLineGap, focal = CameraConfig.read('Camera.cfg')
 
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
    if video.isOpened(): # try to get the first frame
        rval, frame = video.read()
    else:
        return None
    if rval == 1:
        return frame

##    cv2.imwrite('capture.png', frame)
##    cv2.imshow('test',frame)
    
    return None
def grabTestPicture():
    return cv2.imread('test1.jpg')
def getLines():
##    global imageType
    ##img=grabPicture()
    img=grabTestPicture()
    ##if img == None:
        ##return None
    
    if imageType == 'HSV':
        img = cv2.cvtColor(img,cv2.COLOR_BGR2HSV)
    ##img = cv2.imread('test1.jpg')
##        gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
##        im_bw = cv2.threshold(gray, thresh, 255, cv2.THRESH_BINARY)[1]
    ##mask = cv2.inRange(img, lower, upper)
    ##mask = cv2.bitwise_not(mask)
    
    edges = cv2.Canny(img,50,120)

    lines = cv2.HoughLinesP(edges, 1, np.pi/180, thresh, minLineLength, maxLineGap)
    
    print len(lines)

    if len(lines) < 4:
        return None
        ##print 'None'
        
    ##cv2.destroyAllWindows()
    return lines

def lineLength(p1,p2):
    return math.sqrt((p2[0]-p1[0])**2+(p2[1]-p1[1])**2)

def midpt(line):
    midx = (line[0] + line[2])/2
    midy = (line[1] + line[3])/2
    return midx,midy

