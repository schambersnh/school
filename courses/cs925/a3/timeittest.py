import time
import timeit
import numpy

means = []
stddevs = []

numruns = [1,2,5,10,20,50,100, 1000]
for num in numruns:
    results = timeit.repeat("time.sleep(0.0001)", repeat=num, number=1)
    means.append(format(numpy.average(results), '0.10f'))
    stddevs.append(format(numpy.std(results), '0.10f'))
    print "Finished run " + str(num)
print means
print stddevs

