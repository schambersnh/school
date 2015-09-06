import time
import numpy

means = []
stddevs = []
#numruns = [1,2,5,10,20,50,100]
numruns = [1000]
for num in numruns:
	times = []
	for i in range(num):
	    sum = 0
	    t0 = time.time()
	    time.sleep(0.0001)
	    total=time.time() - t0
	    times.append(total)
	print "Finished run " + str(num)
	means.append(format(numpy.average(times), '0.10f'))
	stddevs.append(format(numpy.std(times), '0.10f'))
print means
print stddevs
