#!/bin/bash

./make.sh
mkdir -p outputs_txt
mkdir -p outputs_ps
mkdir -p outputs_pdf
ALG1="depth-first"
ALG2="uniform-cost"
ALG3="depth-first-id"
ALG4="a-star h0"
ALG5="a-star h1"
ALG6="a-star h2"
ALG7="ida-star h0"
ALG8="ida-star h1"
ALG9="ida-star h2"
ALG10="greedy h0"
ALG11="greedy h1"
ALG12="greedy h2"

ALGS=("$ALG1" "$ALG2" "$ALG3" "$ALG4" "$ALG5" "$ALG6" "$ALG7" "$ALG8" "$ALG9" "$ALG10" "$ALG11" "$ALG12")

for alg in "${ALGS[@]}"
do
    alg_output=${alg//[[:space:]]}
    
    #tiny 1
    echo >> outputs_txt/$alg_output.txt
    echo "./vw-validator -time 10 -- ./run.sh $alg < worlds/tiny-1.vw" &>> outputs_txt/$alg_output.txt
    ./vw-validator -time 10 -- ./run.sh $alg < worlds/tiny-1.vw &>> outputs_txt/$alg_output.txt
    
    #tiny 1
    echo >> outputs_txt/$alg_output.txt
    echo "./vw-validator -time 10 -- ./run.sh $alg < worlds/tiny-2.vw" &>> outputs_txt/$alg_output.txt
    ./vw-validator -time 10 -- ./run.sh $alg < worlds/tiny-2.vw &>> outputs_txt/$alg_output.txt

    #tiny 1
    echo >> outputs_txt/$alg_output.txt
    echo "./vw-validator -time 10 -- ./run.sh $alg < worlds/small-1.vw" &>> outputs_txt/$alg_output.txt
    ./vw-validator -time 10 -- ./run.sh $alg < worlds/small-1.vw  &>> outputs_txt/$alg_output.txt

    #tiny 1
    echo >> outputs_txt/$alg_output.txt
    echo "./vw-validator -time 10 -- ./run.sh $alg < worlds/hard-1.vw " &>> outputs_txt/$alg_output.txt
    ./vw-validator -time 10 -- ./run.sh $alg < worlds/hard-1.vw &>> outputs_txt/$alg_output.txt

    #tiny 1
    echo >> outputs_txt/$alg_output.txt
    echo "./vw-validator -time 10 -- ./run.sh $alg < worlds/hard-2.vw" &>> outputs_txt/$alg_output.txt
    ./vw-validator -time 10 -- ./run.sh $alg < worlds/hard-2.vw &>> outputs_txt/$alg_output.txt

    a2ps -2 -o outputs_ps/$alg_output.ps outputs_txt/$alg_output.txt
    ps2pdf outputs_ps/$alg_output.ps outputs_pdf/$alg_output.pdf
done
