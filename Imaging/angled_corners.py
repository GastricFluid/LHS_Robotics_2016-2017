import numpy
import numpy.ndarray.item
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


def Dist_Check(Vector1, Vector2):
    # returns distance between two vectors
    x_diff = abs(Vector1[0] - Vector2[0])
    y_diff = abs(Vector1[0] - Vector2[0])
    distance = math.sqrt(x_diff**2 + y_diff**2)
    return distance


def ArrayAnalyzer(Corner_Array):
    # finds angles of give vector array
    Corner_Angle1 = (AngleCheck(Corner_Array.item(0), Corner_Array.item(1)))
    Corner_Angle2 = (AngleCheck(Corner_Array.item(4), Corner_Array.item(5)))
    Corner_Angle3 = (AngleCheck(Corner_Array.item(2), Corner_Array.item(3)))
    Corner_Angle4 = (AngleCheck(Corner_Array.item(6), Corner_Array.item(7)))
    return (Corner_Angle1, Corner_Angle2, Corner_Angle3, Corner_Angle4)


def CloseState(Corner_Array, min_length, max_length):
    # given a minimum line length and maximum side length return when tape is close
    corner_distance = Dist_Check(Corner_Array(1), Corner_Array(2))
    if corner_distance <= max_length and corner_distance >= min_length:
        return True
    else:
        return False


def TurnState(Analyzed_Angles):
    # determines how to turn based off of given angles)
    angle_total = 0
    angle_num = 0
    for angle in Analyzed_Angles:
        angle_total += angle
        angle_num += 1
    compiled_angle = angle_total/angle_num
    if abs(compiled_angle) >= 85 and abs(compiled_angle):
        return 0
    else:
        if compiled_angle >= 0:
            if abs(compiled_angle) >= 90:
                return 1
            if abs(compiled_angle) < 90:
                return -1
        if compiled_angle < 0:
            if abs(compiled_angle) >= 90:
                return -1
            if abs(compiled_angle) < 90:
                return 1
