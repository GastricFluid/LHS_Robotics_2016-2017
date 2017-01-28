from operator import itemgetter
import math
import numpy as np
import cv2

def corners(lines):
    
    cornerarray=np.array([[lines[2][0][0],lines[2][0][1]],[lines[0][0][0],lines[0][0][1]],[lines[3][0][0],lines[3][0][1]],[lines[1][0][0],lines[1][0][1]],[lines[2][0][2],lines[2][0][2]],[lines[0][0][2],lines[0][0][3]],[lines[3][0][2],lines[3][0][3]],[lines[1][0][2],lines[1][0][3]]])
                          
    return cornerarray
    
##    i=0
##    n=0
##    for line in lines:
##        x1,y1,x2,y2 = line[0]
##        if i<4:
##            np.insert(cornerarray,[0],[x1,y1])
##            np.insert(cornerarray,[1],[x2,y2])
##            print corners
##            i+=1
##    n=1
##    while n<4:
##        b=[]
##        b = cornerarray[n]
##        cornerarray = delete(cornerarray, [n], axis=0)
##        np.insert(cornerarray,n+4,b, axis=0)
##        n+=1
##    ##corners=sorted(corners, key=itemgetter(1), reverse=True)
##    ##corners=sorted(corners, key=itemgetter(0))
    

def length(p1,p2):
    return math.sqrt((p2[0]-p1[0])**2+(p2[1]-p1[1])**2)

def midpt(line):
    midx = (line[0] + line[2])/2
    midy = (line[1] + line[3])/2
    return midx,midy

def finalcorners():
    img = cv2.imread('test1.jpg')
    gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    thresh = 127
    im_bw = cv2.threshold(gray, thresh, 255, cv2.THRESH_BINARY)[1]
    edges = cv2.Canny(im_bw,50,120)
    minLineLength = 50
    maxLineGap = 5

    lines = cv2.HoughLinesP(edges, 1, np.pi/180, 127, minLineLength, maxLineGap)
    return corners(lines)
