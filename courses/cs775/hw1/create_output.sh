#!/bin/bash
for i in `seq 0 19`;
    do
        java -jar ra.jar -i q$i -o out$i
    done    

