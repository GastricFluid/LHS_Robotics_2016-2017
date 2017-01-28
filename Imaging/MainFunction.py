import angled_corners
import line


def CornerInput():
    return line.finalcorners()


def AngleOut(CornerArray):
    analyzed_angles = angled_corners.ArrayAnalyzer(CornerArray)
    print angled_corners.CompiledAnglePure(analyzed_angles)


def Main():
    while True:
        if raw_input():
            break
        given_corners = CornerInput()
        AngleOut(given_corners)

if __name__ == '__main__':
    Main()
