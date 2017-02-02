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


def calibrate(knownDistance):
    lines=getLines()

    calculatedPixels = lineLength(midpt(lines[0][0]),midpt(lines[2][0]))

    focal= calibrateCamera.determineFocal(knownDistance, 2, calculatedPixels)
    #Store focal in file
    CameraConfig.write([127, 50, 5, focal])
    
def init():
    #Read focal point from file
    global thresh, minLineLength, maxLineGap, focal

    thresh, minLineLength, maxLineGap, focal = CameraConfig.read()

    return

def run():
    return

def view():
    if video.isOpened(): # try to get the first frame
        rval, frame = video.read()
    else:
        rval = False

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
        rval = False

    while rval:
        ret, frame = video.read()
        if ret == 1:
            break

##    cv2.imwrite('capture.png', frame)
##    cv2.imshow('test',frame)
    
    return frame

def getLines():
    while True:
        img=grabPicture()
        ##img = cv2.imread('test1.jpg')
        gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
        im_bw = cv2.threshold(gray, thresh, 255, cv2.THRESH_BINARY)[1]
        edges = cv2.Canny(im_bw,50,120)

        lines = cv2.HoughLinesP(edges, 1, np.pi/180, thresh, minLineLength, maxLineGap)

        if lines is None:
            continue
            ##print 'None'
        else:
            ##print lines.size, len(lines)
            if len(lines) >= 4:
                ##print lines
                break
    cv2.destroyAllWindows()
    return lines

def lineLength(p1,p2):
    return math.sqrt((p2[0]-p1[0])**2+(p2[1]-p1[1])**2)

def midpt(line):
    midx = (line[0] + line[2])/2
    midy = (line[1] + line[3])/2
    return midx,midy
