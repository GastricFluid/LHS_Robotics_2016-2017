import pickle
fileName='Camera.cfg'

def write(values, fileName):
    with open(fileName, 'w') as f:
        pickle.dump(values, f)

def read(fileName):
    with open(fileName, 'r') as f:
        return pickle.load(f)

def RGBorHSV():
    inp = raw_input('RGB or HSV?')
    inp = inp.lower()
    print inp
    if inp == 'r':
        return 'RGB'
    elif inp == 'h':
        return 'HSV'
    else:
        return RGBorHSV()

