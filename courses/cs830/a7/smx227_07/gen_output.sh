for filename in cnfs/*.cnf
do
    echo >> a6-transcript
    echo >> a6-transcript
    echo >> a6-transcript
    echo "************************************************************************************" >> a6-transcript
    echo "./unify-validator ./run.sh < $filename >> a6-transcript" >> a6-transcript
    ./unify-validator ./run.sh < $filename >> a6-transcript
    echo "************************************************************************************" >> a6-transcript
done
