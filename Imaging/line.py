from operator import itemgetter
import math
import numpy as np
import cv2
import Camera

def corners(lines):
    print lines
    if lines == None or lines[0] == None:
        return None
    elif len(lines[0]) == 1:
        cornerarray=np.array([[lines[2][0][0], lines[2][0][1]],
                            [lines[0][0][0], lines[0][0][1]],
                            [lines[3][0][0], lines[3][0][1]],
                            [lines[1][0][0], lines[1][0][1]],
                            [lines[2][0][2], lines[2][0][3]],
                            [lines[0][0][2], lines[0][0][3]],
                            [lines[3][0][2], lines[3][0][3]],
                            [lines[1][0][2], lines[1][0][3]]])
        return cornerarray
    elif len(lines[0]) > 1:
        cornerarray=np.array([[lines[0][2][0], lines[0][2][1]],
                            [lines[0][0][0], lines[0][0][1]],
                            [lines[0][3][0], lines[0][3][1]],
                            [lines[0][1][0], lines[0][1][1]],
                            [lines[0][2][2], lines[0][2][3]],
                            [lines[0][0][2], lines[0][0][3]],
                            [lines[0][3][2], lines[0][3][3]],
                            [lines[0][1][2], lines[0][1][3]]])                  
        return cornerarray

    return None
    
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
    



def finalcorners():    
    lines = Camera.getLines()
    
    return corners(lines)
