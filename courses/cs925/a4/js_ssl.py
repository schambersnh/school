import time
import os
import numpy
import requests

NUM_RUNS = 1000
times = []
for i in range(NUM_RUNS):
	t0 = time.time()
	requests.get('https://192.168.0.1:1234/~smx227/hello.txt', verify=False, cert=('rb2.crt', 'rb2.key'))
        tfinal = time.time() - t0
	times.append(tfinal)
mean = numpy.average(times)
stddev = numpy.std(times)
print mean
print stddev
