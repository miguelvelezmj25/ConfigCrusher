#!/usr/bin/env bash

PROGRAM_CLASS_PATH=${1}
MAIN_CLASS=${2}
ARG0=${3}
ARG1=${4}
ARG2=${5}
ARG3=${6}
ARG4=${7}
OUTPUT_FILE=data.ser

rm -rf $OUTPUT_FILE
(/usr/bin/time -p java -Xmx12g -Xms12g -cp $PROGRAM_CLASS_PATH $MAIN_CLASS $ARG0 $ARG1 $ARG2 $ARG3 $ARG4) 2>$OUTPUT_FILE
