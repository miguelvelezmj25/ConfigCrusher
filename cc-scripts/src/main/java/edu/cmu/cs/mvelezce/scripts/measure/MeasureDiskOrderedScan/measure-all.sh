#!/usr/bin/env bash

BASE=$(pwd)
#echo $BASE
cd $BASE/../../../../../../../../../../../ || exit
CC_ROOT=$(pwd)
#echo $CC_ROOT

#cd ./cc-execute-e2e/ || exit
#echo ""
#echo "Running Berkeley DB GT"
#echo ""
#mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.gt.GroundTruthExecutorTest#berkeleyDb
#echo ""
#echo "Done with Berkeley DB GT"
#echo ""
#sleep 60s
#cd ../ || exit
#
#cd ./cc-execute-e2e/ || exit
#echo ""
#echo "Running Berkeley DB BF"
#echo ""
#mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.bf.BruteForceExecutorTest#berkeleyDb
#echo ""
#echo "Done with Berkeley DB BF"
#echo ""
#sleep 60s
#cd ../ || exit

cd ./cc-execute-e2e/ || exit
echo ""
echo "Running Berkeley DB FW"
echo ""
mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.fw.FeatureWiseExecutorTest#berkeleyDb
echo ""
echo "Done with Berkeley DB FW"
echo ""
sleep 60s
cd ../ || exit

cd ./cc-execute-e2e/ || exit
echo ""
echo "Running Berkeley DB PW"
echo ""
mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.pw.PairWiseExecutorTest#berkeleyDb
echo ""
echo "Done with Berkeley DB PW"
echo ""
sleep 60s
cd ../ || exit

#cd ./cc-execute/ || exit
#echo ""
#echo "Running Berkeley DB IDTA"
#echo ""
#mvn test -Dtest=edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutorTest#berkeleyDb
#echo ""
#echo "Done with Berkeley DB IDTA"
#echo ""
#sleep 60s
#cd ../ || exit

#cd ./cc-execute/ || exit
#echo ""
#echo "Running Berkeley DB SPLat"
#echo ""
#mvn test -Dtest=edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutorTest#berkeleyDb
#echo ""
#echo "Done with Berkeley DB SPLat"
#echo ""
#sleep 60s
#cd ../ || exit
