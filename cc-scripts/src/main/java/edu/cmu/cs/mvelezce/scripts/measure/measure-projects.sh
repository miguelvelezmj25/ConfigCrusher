#!/usr/bin/env bash

NULL="null"
PROGRAMS="berkeleyDb lucene density"
GT="gt"
BF="bf"
FW="fw"
PW="pw"
IDTA="idta"
SPLAT="splat"
BASE=$(pwd)

cd "$BASE"/../../../../../../../../../../ || exit

program=$NULL
approach=$NULL

run() {
  local program=$1
  local approach=$2

  echo ""
  echo "Running" "$program" "$approach"
  echo ""

  if [ "$approach" == $GT ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.gt.GroundTruthExecutorTest#"$program"
  elif [ "$approach" == $BF ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.bf.BruteForceExecutorTest#"$program"
  elif [ "$approach" == $FW ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.fw.FeatureWiseExecutorTest#"$program"
  elif [ "$approach" == $PW ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.pw.PairWiseExecutorTest#"$program"
  elif [ "$approach" == $IDTA ]; then
    cd ./cc-execute/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutorTest#"$program"
  elif [ "$approach" == $SPLAT ]; then
    echo "Need to implement SPLAT"
  fi

  echo ""
  echo "Done with" "$program" "$approach"
  echo ""
  sleep 60s

  cd .. || exit
}

for entry in "$@"; do
  if [[ $PROGRAMS =~ (^|[[:space:]])$entry($|[[:space:]]) ]]; then
    program=$entry
    approach=$NULL

    continue
  else
    approach=$entry
  fi

  if [ "$program" == $NULL ]; then
    echo "The program is null"

    continue
  fi

  if [ "$approach" == $NULL ]; then
    echo "The approach is null"

    continue
  fi

  if [ "$approach" != $GT ] && [ "$approach" != $BF ] && [ "$approach" != $FW ] && [ "$approach" != $PW ] && [ "$approach" != $IDTA ] && [ "$approach" != $SPLAT ]; then
    echo "Could not find approach" "$approach"
    program=$NULL
    approach=$NULL
  else
    run $program $approach
  fi

done
