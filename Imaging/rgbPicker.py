import cv2
import numpy as np
import Camera

def nothing(x):
    pass

# Create a black image, a window
cv2.namedWindow('image', cv2.WINDOW_NORMAL)


# create trackbars for color change
cv2.createTrackbar('Red Low','image',0,255,nothing)
cv2.createTrackbar('Green Low','image',0,255,nothing)
cv2.createTrackbar('Blue Low','image',0,255,nothing)
cv2.createTrackbar('Red High','image',0,255,nothing)
cv2.createTrackbar('Green High','image',0,255,nothing)
cv2.createTrackbar('Blue High','image',0,255,nothing)

img = Camera.grabPicture()

while(1):
    k = cv2.waitKey(1) & 0xFF
    if k == 27:
        break

    # get current positions of six trackbars
    rL = cv2.getTrackbarPos('Red Low','image')
    gL = cv2.getTrackbarPos('Green Low','image')
    bL = cv2.getTrackbarPos('Blue Low','image')
    rH = cv2.getTrackbarPos('Red High','image')
    gH = cv2.getTrackbarPos('Green High','image')
    bH = cv2.getTrackbarPos('Blue High','image')
    lower = np.array([rL,gL,bL], dtype=np.uint8)
    upper = np.array([rH,gH,bH], dtype=np.uint8)

    mask = cv2.inRange(img, lower, upper)
    mask = cv2.bitwise_not(mask)
    cv2.imshow('image',mask)

cv2.destroyAllWindows()
