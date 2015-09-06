#!/bin/bash
for i in `seq 0 19`;
    do
        java -jar ra.jar -i q$i -o greg$i
    done
echo "********************CALCULATING DIFFERENCES********************"
echo "Note: If nothing appears below the 'Differences in qx' line, there are no differences for that query"
for i in `seq 0 19`;
    do
	echo "********Differences in q"$i"***********"
        diff out$i greg$i
    done	

