#!/usr/bin/env bash

BASE=$(pwd)
#echo $BASE
cd $BASE/../../../../../../../../../../../ || exit
CC_ROOT=$(pwd)
#echo $CC_ROOT

cd ./cc-execute-e2e/ || exit
echo ""
echo "Running Lucene GT"
echo ""
mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.gt.GroundTruthExecutorTest#lucene
echo ""
echo "Done with Lucene GT"
echo ""
sleep 60s
cd ../ || exit
