import cv2
import numpy as np
import math

img = cv2.imread('test1.jpg')
##img = cv2.resize(img, (0,0), fx=0.2, fy=0.2)
gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
thresh = 127
im_bw = cv2.threshold(gray, thresh, 255, cv2.THRESH_BINARY)[1]
edges = cv2.Canny(im_bw,50,120)
minLineLength = 50
maxLineGap = 5

lines = cv2.HoughLinesP(edges, 1, np.pi/180, 127, minLineLength, maxLineGap)
print lines

def midpt(line):
    midx = (line[0] + line[2])/2
    midy = (line[1] + line[3])/2
    return midx,midy

def length(p1,p2):
    return math.sqrt((p2[0]-p1[0])**2+(p2[1]-p1[1])**2)

def determineFocal(knownDistance, knownWidth, calculatedLength):
    return calculatedLength * knownDistance / knownWidth

def calculateDistance(focal, width, calculatedLength):
    return focal * width / calculatedLength


i=0
for line in lines:
    x1,y1,x2,y2 = line[0]
    i+=1
    print x1, y1, x2, y2
    if i <3:
        cv2.line(img, (x1,y1), midpt(line[0]), (0,255,0), 2)
    else:    
        cv2.line(img, (x1,y1), (x2,y2), (0,0,255), 2)
    
calculatedPixels = length(midpt(lines[0][0]),midpt(lines[2][0]))
print calculatedPixels
print length(midpt(lines[2][0]),midpt(lines[3][0]))
print length(midpt(lines[2][0]),midpt(lines[1][0]))

focal = determineFocal(24, 2, calculatedPixels)
print focal    

distance = calculateDistance(focal, 2, calculatedPixels)
print distance

cv2.imshow("edges", img)
cv2.imshow("lines", im_bw)
cv2.waitKey()
cv2.destroyAllWindows()
