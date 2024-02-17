import math, re, sys
from numpy import *

# usage: python tsp-verifier.py inputfilename solutionfilename

def main(instancefile, solutionfile):
    cities = readinstance(instancefile)
    solution = readsolution(solutionfile)
    checksolution(cities, solution[0][0],solution[1])


def distance(a, b):
    # a and b are integer pairs (each representing a point in a 2D, integer grid)
    # Euclidean distance rounded to the nearest integer:
    dx = a[0] - b[0]
    dy = a[1] - b[1]
    # return int(math.sqrt(dx*dx + dy*dy)+0.5) # equivalent to the next line
    return int(round(math.sqrt(dx * dx + dy * dy)))


def readinstance(filename):
    # each line of input file represents a city given by three integers:
    # identifier x-coordinate y-coordinate (space separated)
    # city identifiers are always consecutive integers starting with 0
    # (although this is not assumed here explicitly,
    #    it will be a requirement to match up with the solution file)
    f = open(filename, 'r')
    line = f.readline()
    cities = []
    while len(line) > 1:
        lineparse = re.findall(r'[^,;\s]+', line)
        cities.append([int(lineparse[1]), int(lineparse[2])])
        line = f.readline()
    f.close()
    return cities


def readsolution(filename):
    # first line is the length of the solution
    # remaining lines are the cities in the order they are visited
    # each city is listed once
    # cities are identified by integers from 0 to n-1

    # read in tour length
    f = open(filename, 'r')
    value = int(f.readline())

    # read in cities
    solution = []
    line = f.readline()
    while len(line) > 1:
        lineparse = re.findall(r'[^,;\s]+', line)
        solution.append(int(lineparse[0]))
        line = f.readline()
    f.close()

    return [[value], solution]


def checksolution(cities,value,cityorder):

    n = len(cities)
    cityorder_sorted = list(cityorder);
    cityorder_sorted.sort()
    n_half = len(cityorder_sorted);

    # check all n/2 cities here
    if (n_half !=  math.ceil(n / 2.0)):
        print('ERROR: Not a half tour')
        exit();
    else:
        for i in range(len(cityorder)-1):
            #check duplicate citites
            if(cityorder_sorted [i] == cityorder_sorted[i+1]):
                print('ERROR: There are duplicate cities')
                exit();
        for i in range(len(cityorder)):
            #check invalid city id
            if ((cityorder[i] < 0 ) | (cityorder[i] >= n)):
                print ('ERROR: Invalid city id: ',cityorder[i])
                exit();
    # calculate the length of the tour given by cityorder:
    dist = 0
    for i in range(n_half):
        dist = dist + distance(cities[cityorder[i]], cities[cityorder[i - 1]])

    # check value of solution
    if dist == value:
        print('Your solution is VERIFIED. ')
    else:
        print('Your solution is NOT VERIFIED.')
        print('Your solution length given as ', value)
        print('but computed as ', dist)


main(sys.argv[1], sys.argv[2])
