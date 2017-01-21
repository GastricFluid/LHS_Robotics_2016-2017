import numpy
import numpy.ndarray.item
import cv2
import math


def AngleCheck(Vector1, Vector2):
    # finds the angle between two vectors
    x_diff = (Vector1[0] - Vector2[0])
    y_diff = (Vector1[1] - Vector2[1])
    if x_diff != 0:
        comp_rad = math.atan2(x_diff, y_diff)
    else:
        comp_rad = math.pi/2
    comp_angle = math.degrees(comp_rad)
    return comp_angle


def ArrayAnalyzer(Corner_Array):
    # finds angles of give vector array
    Corner_Angle1 = (AngleCheck(Corner_Array.item(0), Corner_Array.item(1)))
    Corner_Angle2 = (AngleCheck(Corner_Array.item(4), Corner_Array.item(5)))
    Corner_Angle3 = (AngleCheck(Corner_Array.item(2), Corner_Array.item(3)))
    Corner_Angle4 = (AngleCheck(Corner_Array.item(6), Corner_Array.item(7)))
    return (Corner_Angle1, Corner_Angle2, Corner_Angle3, Corner_Angle4)
