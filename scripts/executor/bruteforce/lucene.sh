#!/usr/bin/env bash

CLASS_DIR=${1}

PROGRAM_NAME=lucene
ENTRY_POINT=org.apache.lucene.demo.IndexFiles
ITERATIONS=5

M2=$(echo $HOME)/.m2/repository
CC=./target/classes

function run {
    local cc=$1
    local m2=$2
    local program_name=$3
    local class_dir=$4
    local entry_point=$5
    local iterations=$6

    java -cp \
      $cc:$m2/commons-io/commons-io/2.5/commons-io-2.5.jar:$m2/org/apache/commons/commons-math3/3.6.1/commons-math3-3.6.1.jar:$m2/commons-cli/commons-cli/1.4/commons-cli-1.4.jar:$m2/com/fasterxml/jackson/core/jackson-core/2.8.9/jackson-core-2.8.9.jar:$m2/com/fasterxml/jackson/core/jackson-databind/2.8.9/jackson-databind-2.8.9.jar:$m2/com/fasterxml/jackson/core/jackson-annotations/2.8.9/jackson-annotations-2.8.9.jar \
      edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor \
      $program_name \
      $class_dir \
      $entry_point \
      $iterations
}

(
cd ../../../
run $CC $M2 $PROGRAM_NAME $CLASS_DIR $ENTRY_POINT $ITERATIONS
)