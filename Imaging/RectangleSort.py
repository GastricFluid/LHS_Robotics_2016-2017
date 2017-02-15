import numpy as np
import math

def sortRectangles(edges, numRectanglesToFind):
    if edges is None or numRectanglesToFind is None or numRectanglesToFind < 1:
        return None

    return orderEdges(findRectangles(edges, numRectanglesToFind*2))


def findRectangles(lines,edgeCountWanted):
    sortedArray = []
    for line in lines:
##        print line
        if len(sortedArray) == 0:
            sortedArray.append(line)
        else:
            for i in range(len(sortedArray)):
                inserted = False
                if line[0][1] > sortedArray[i][0][1]:
##                    print line[0][1]
##                    print sortedArray[i][0][1]
                    sortedArray.insert(i,line)
##                    print sortedArray
                    inserted = True
                    break
                elif line[0][1] == sortedArray[i][0][1]:
                    if line[0][0] > sortedArray[i][0][0]:
                        sortedArray.insert(i,line)
                    else:
                        sortedArray.insert(i+1,line)
                    inserted = True
                    break

            if inserted == False:
                sortedArray.append(line)

    if len(sortedArray) <= edgeCountWanted:
        return np.array(sortedArray)
    else:
        return np.array(sortedArray[0:edgeCountWanted])

def orderEdges(lines):
    sortedArray = []
    for line in lines:
##        print line
        if len(sortedArray) == 0:
            sortedArray.append(line)
        else:
            for i in range(len(sortedArray)):
                inserted = False
                if line[0][0] < sortedArray[i][0][0]:
##                    print line[0][0]
##                    print sortedArray[i][0][0]
                    sortedArray.insert(i,line)
##                    print sortedArray
                    inserted = True
                    break
            if inserted == False:
                sortedArray.append(line)

    return np.array(sortedArray)


##edges = [[280,75,280,54]],[[158,155,158,143]],[[142,156,142,142]],[[164,156,164,142]],[[135,154,135,143]]
##edges = np.array(edges)
##
##print sortRectangles(edges,2)
    
        
        
