##Functions used for calibrating the cameras

##This function determines the Focal Length of the camera
def determineFocal(knownDistance, knownWidth, calculatedLength):
    return calculatedLength * knownDistance / knownWidth

##This function calculates the straight line distance the camera is from
##a known object
def calculateDistance(focal, width, calculatedLength):
    return focal * width / calculatedLength
