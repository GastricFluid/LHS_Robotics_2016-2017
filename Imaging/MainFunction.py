import angled_corners
import line
import serial
import Camera
import time


def CornerInput():
    return line.finalcorners()


def AngleOut(CornerArray):
    analyzed_angles = angled_corners.ArrayAnalyzer(CornerArray)
    return angled_corners.CompiledAnglePure(analyzed_angles)


def SerTest():

	ser = serial.Serial('/dev/ttyUSB0', 9600, timeout=0)

	#ser = serial.Serial('/dev/ttyAMA0', 9600, timeout=0)
	

	angle = 90
	ser.write(str(angle).encode())
	print(ser.readline())
	

def Main():
	Camera.init()
	waitloop = 0

	ser = serial.Serial('/dev/ttyUSB0', 9600, timeout=0)

	#ser = serial.Serial('/dev/ttyAMA0', 9600, timeout=0)
	

	print("ook")
	while True:
		print("in while")
		given_corners = CornerInput()
		print(given_corners)
		if given_corners != None:
			angle = AngleOut(given_corners)
			print(angle)
			ser.write(str(angle).encode())
			#print(ser.readline())
			while ser.out_waiting() != 0 and waitloop <= 11:
				waitloop += 1
				pass
			ser.reset_output_buffer()
			waitloop = 0 
	ser.close()


if __name__ == '__main__':
    Main()
