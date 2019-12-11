#!/usr/bin/env bash

BASE=$(pwd)
#echo $BASE
cd $BASE/../../../../../../../../../../../ || exit
CC_ROOT=$(pwd)
#echo $CC_ROOT

cd ./cc-execute/ || exit
echo ""
echo "Running Berkeley DB IDTA"
echo ""
mvn test -Dtest=edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutorTest#berkeleyDb
echo ""
echo "Done with Berkeley DB IDTA"
echo ""
sleep 60s
cd ../ || exit
