import cv2
import numpy as np
import Camera

def nothing(x):
    pass

# Create a black image, a window
cv2.namedWindow('image', cv2.WINDOW_NORMAL)


# create trackbars for color change
cv2.createTrackbar('Hue Low','image',0,255,nothing)
cv2.createTrackbar('Sat Low','image',0,255,nothing)
cv2.createTrackbar('Value Low','image',0,255,nothing)
cv2.createTrackbar('Hue High','image',0,255,nothing)
cv2.createTrackbar('Sat High','image',0,255,nothing)
cv2.createTrackbar('Value High','image',0,255,nothing)

img = Camera.grabPicture()
img = cv2.cvtColor(img,cv2.COLOR_BGR2HSV)

while(1):
    k = cv2.waitKey(1) & 0xFF
    if k == 27:
        break

    # get current positions of six trackbars
    hL = cv2.getTrackbarPos('Hue Low','image')
    sL = cv2.getTrackbarPos('Sat Low','image')
    vL = cv2.getTrackbarPos('Value Low','image')
    hH = cv2.getTrackbarPos('Hue High','image')
    sH = cv2.getTrackbarPos('Sat High','image')
    vH = cv2.getTrackbarPos('Value High','image')
    lower = np.array([hL,sL,vL], dtype=np.uint8)
    upper = np.array([hH,sH,vH], dtype=np.uint8)

    mask = cv2.inRange(img, lower, upper)
    mask = cv2.bitwise_not(mask)
    cv2.imshow('image',mask)

cv2.destroyAllWindows()
