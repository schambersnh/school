import time
import os
import numpy
import requests

NUM_RUNS = 1000
times = []

for i in range(NUM_RUNS):
	t0 = time.time()
	r = requests.get('http://192.168.0.1:8080/~smx227/hello.txt')
        tfinal = time.time() - t0
	times.append(tfinal)
mean = numpy.average(times)
stddev = numpy.std(times)
print mean
print stddev
