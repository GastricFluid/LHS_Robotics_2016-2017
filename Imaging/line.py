from operator import itemgetter
import math

def corners(lines):
    
    corners=[[],[]]

    i=0
    for line in lines:
        x1,y1,x2,y2 = line[0]
        i+=1
        if i<5:
            corners[0].append((x1,y1))
            corners[1].append((x2,y2))

    corners[0]=sorted(corners[0], key=itemgetter(0))
    corners[1]=sorted(corners[1], key=itemgetter(0))
    return corners

def length(p1,p2):
    return math.sqrt((p2[0]-p1[0])**2+(p2[1]-p1[1])**2)

def midpt(line):
    midx = (line[0] + line[2])/2
    midy = (line[1] + line[3])/2
    return midx,midy
