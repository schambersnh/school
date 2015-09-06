#!/bin/bash
for i in `seq 1 15`;
    do
        echo "***** Query $i *****"
        mysql --user smx227 --password=RuacAm0 smx227 < query$i.sql > outputs/o$i
        cat outputs/o$i
        echo
        echo
    done
        
