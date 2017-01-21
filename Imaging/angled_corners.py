import numpy
import cv2
import math


def AngleCheck(Vector1, Vector2):
    x_diff = (Vector1[0] - Vector2[0])
    y_diff = (Vector1[1] - Vector2[1])
    if x_diff != 0:
        comp_rad = math.atan2(x_diff, y_diff)
    else:
        comp_rad = math.pi/2
    comp_angle = math.degrees(comp_rad)
    print y_diff
    print x_diff
    print comp_rad
    return comp_angle


def ArraySetter():
    pass

print AngleCheck([5, 0], [6, 1])
