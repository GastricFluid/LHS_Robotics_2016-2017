import pickle
fileName='Camera.cfg'

def write(values, fileName):
    with open(fileName, 'w') as f:
        pickle.dump(values, f)

def read(fileName):
    with open(fileName, 'r') as f:
        return pickle.load(f)

