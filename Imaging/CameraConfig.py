import pickle
fileName='Camera.cfg'

def write(values):
    with open(fileName, 'w') as f:
        pickle.dump(values, f)

def read():
    with open(fileName, 'r') as f:
        return pickle.load(f)

