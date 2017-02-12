import cv2
import numpy as np
import picamera
from picamera import PiCamera
from picamera.array import PiRGBArray

#camera module setup (ribbon cable, not USB)
cols = 640
rows = 480
camera = PiCamera()
camera.resolution = (cols,rows)
camera.framerate = 24
rawCapture = PiRGBArray(camera,size=(cols,rows))

###UI trackbars for HSV threshold values
def trackbars():
	def nothing(x):
		pass
	cv2.namedWindow('img',0)
	cv2.createTrackbar('HUE','img',0,255,nothing)
	cv2.createTrackbar('SAT','img',0,255,nothing)
	cv2.createTrackbar('VAL','img',0,255,nothing)
	cv2.resizeWindow('img',640,480)
	lb = np.array([45,75,75])
	ub = np.array([70,255,255])

def bounds():
	hue = cv2.getTrackbarPos('HUE','img')
	sat = cv2.getTrackbarPos('SAT','img') 
	val = cv2.getTrackbarPos('VAL','img') 

	lb = np.array([(hue-20),sat,val])
	ub = np.array([(hue+20),255,255])

	return lb, ub

def bounds2():
	#overrides
	lb = np.array([50,0,200])
	ub = np.array([90,255,255])
	return lb, ub

def findcorners(img):
	#edge and corner detection using piggybacked Canny, Harris and Shi-Tomasi
	edges = cv2.Canny(img,150,200)
	corners = cv2.cornerHarris(edges,5,5,.1)    
	corners = cv2.goodFeaturesToTrack(corners,8,0.01,20)
	if corners is not None:
		#convert to ints and plot circles at coordinates		
		corners = np.int0(corners)
		for i in corners:
		    x,y = i.ravel()
		    cv2.circle(frame,(x,y),5,(255,0,255),-1)


### MAIN LOOP ###

#trackbars()

for frame in camera.capture_continuous(rawCapture, format='bgr',use_video_port=True):
	#get frame, rotate 90 clockwise (camera is sidways on bot), convert to HSV space, threshold on HSV values
	frame = frame.array
	M = cv2.getRotationMatrix2D((cols/2,rows/2),-90,1)
	frame = cv2.warpAffine(frame,M,(cols,rows))
	hsv = cv2.cvtColor(frame,cv2.COLOR_BGR2HSV)
	#lb,ub = bounds()
	lb,ub = bounds2()
	img = cv2.inRange(hsv, lb,ub)
	#morphological opening to solidify contours
	kernel = np.ones((3,3),np.uint8)
	img = cv2.GaussianBlur(img,(5,5),0)   
	img = cv2.morphologyEx(img, cv2.MORPH_OPEN, kernel)
	 
	#find corners
	findcorners(img)

	#convert any grayscales to BGR to display all side by side
	img = cv2.cvtColor(img,cv2.COLOR_GRAY2BGR)
	final = np.concatenate((frame, hsv, img),axis=1)
	cv2.imshow('img',final)
	cv2.moveWindow('img',0,0)
	key = cv2.waitKey(1) & 0xFF
	rawCapture.truncate(0)
	if key == ord('q'):
		break
