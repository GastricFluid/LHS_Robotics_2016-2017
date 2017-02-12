import pickle
import os
fileName='Camera.cfg'

def write(values, fileName):
    filename = os.path.join(os.path.dirname(os.path.realpath(__file__)), fileName)
    with open(filename, 'wb') as f:
        pickle.dump(values, f)

def read(fileName):
    filename = os.path.join(os.path.dirname(os.path.realpath(__file__)), fileName)
    with open(filename, 'rb') as f:
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
