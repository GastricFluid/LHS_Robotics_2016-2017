import pickle
fileName='Camera.cfg'

def write(values, fileName):
    with open(fileName, 'wb') as f:
        pickle.dump(values, f)

def read(fileName):
    with open(fileName, 'rb') as f:
        return pickle.load(f)

def RGBorHSV():
    inp = input('RGB or HSV?')
    inp = inp.lower()
    if inp == 'r':
        return 'RGB'
    elif inp == 'h':
        return 'HSV'
    else:
        return RGBorHSV()

