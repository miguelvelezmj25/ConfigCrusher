#!/usr/bin/env bash

NULL="null"
PROGRAMS="berkeleyDb lucene density multithread"
GTInst="gtinst"
BFInst="bfinst"
FWInst="fwinst"
PWInst="pwinst"
GTTime="gttime"
BFTime="bftime"
FWTime="fwtime"
PWTime="pwtime"
IDTA="idta"
IDTAE2EInst="idtae2einst"
IDTAE2ETime="idtae2etime"
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

  if [ "$approach" == $GTInst ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.instrument.gt.GroundTruthInstrumentExecutorTest#"$program"
  elif [ "$approach" == $BFInst ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.instrument.bf.BruteForceInstrumentExecutorTest#"$program"
  elif [ "$approach" == $FWInst ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.instrument.fw.FeatureWiseInstrumentExecutorTest#"$program"
  elif [ "$approach" == $PWInst ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.instrument.pw.PairWiseInstrumentExecutorTest#"$program"
  elif [ "$approach" == $GTTime ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.time.gt.GroundTruthTimeExecutorTest#"$program"
  elif [ "$approach" == $BFTime ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.time.bf.BruteForceExecutorTest#"$program"
  elif [ "$approach" == $FWTime ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.time.fw.FeatureWiseExecutorTest#"$program"
  elif [ "$approach" == $PWTime ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.time.pw.PairWiseExecutorTest#"$program"
  elif [ "$approach" == $IDTA ]; then
    cd ./cc-execute/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutorTest#"$program"
  elif [ "$approach" == $IDTAE2EInst ]; then
    cd ./cc-execute-e2e/ || exit
    mvn test -Dtest=edu.cmu.cs.mvelezce.e2e.execute.instrument.idta.IDTAInstrumentExecutorTest#"$program"
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

  if [ "$approach" != $GTInst ] && [ "$approach" != $BFInst ] && [ "$approach" != $FWInst ] && [ "$approach" != $PWInst ] && [ "$approach" != $IDTA ] && [ "$approach" != $IDTAE2EInst ] && [ "$approach" != $IDTAE2ETime ] && [ "$approach" != $GTTime ] && [ "$approach" != $BFTime ] && [ "$approach" != $FWTime ] && [ "$approach" != $PWTime ] && [ "$approach" != $SPLAT ]; then
    echo "Could not find approach" "$approach"
    program=$NULL
    approach=$NULL
  else
    run $program $approach
  fi

done
