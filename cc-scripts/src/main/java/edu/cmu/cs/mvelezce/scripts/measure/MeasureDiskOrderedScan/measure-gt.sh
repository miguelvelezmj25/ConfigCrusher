#!/usr/bin/env bash

BASE=$(pwd)
#echo $BASE
cd $BASE/../../../../../../../../../../../ || exit
CC_ROOT=$(pwd)
#echo $CC_ROOT

cd ./cc-execute-e2e/ || exit
echo ""
echo "Running Berkeley DB GT"
echo ""
mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.gt.GroundTruthExecutorTest#berkeleyDb
echo ""
echo "Done with Berkeley DB GT"
echo ""
sleep 60s
cd ../ || exit
