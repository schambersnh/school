import time
import os
import numpy
import requests

prev = 0
prediction = 0.0
NUM_RUNS = 1000
NUM_AVG = 50
fail_count = 0
scale = 0.94
times = []
utils = []
for i in range(NUM_RUNS):
        t0 = time.time()
	r = requests.get('http://192.168.0.1:8080/~smx227/cs925.test')
        tfinal = time.time() - t0
        if i == 0:
            prediction = tfinal
        else: 
            if prediction > tfinal:
                print "FAIL"
                fail_count += 1
            else:
                print "PASS"
	    times.append(tfinal)
            mov_avg_list = times[-NUM_AVG:]
            mov_avg = numpy.average(times)
            prediction = (mov_avg) * scale
            util = prediction / tfinal
            utils.append(util)
            print tfinal
            print "Next value predicted to be " + str(prediction)
print "Results: " + str(NUM_RUNS - fail_count) + "/" + str(NUM_RUNS)
print "Average utilization: " + str(numpy.average(utils))
