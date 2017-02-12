import cv2
import numpy as np
import calibrate
import line
##import angled_corners

img = cv2.imread('test1.jpg')
gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
thresh = 127
im_bw = cv2.threshold(gray, thresh, 255, cv2.THRESH_BINARY)[1]
edges = cv2.Canny(im_bw,50,120)
minLineLength = 50
maxLineGap = 5

lines = cv2.HoughLinesP(edges, 1, np.pi/180, 127, minLineLength, maxLineGap)
print(lines)

print('corners:')
print(line.finalcorners())
calculatedPixels = line.length(line.midpt(lines[0][0]),line.midpt(lines[2][0]))
print(calculatedPixels)
print(line.length(line.midpt(lines[2][0]),line.midpt(lines[3][0])))
print(line.length(line.midpt(lines[2][0]),line.midpt(lines[1][0])))




focal = calibrate.determineFocal(24, 2, calculatedPixels)
print(focal)    

distance = calibrate.calculateDistance(focal, 2, calculatedPixels)
print(distance)

##cv2.imshow("edges", img)
##cv2.imshow("lines", im_bw)
cv2.waitKey()
cv2.destroyAllWindows()
