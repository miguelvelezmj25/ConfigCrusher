package edu.cmu.cs.mvelezce.e2e.execute.time.parser.results;

import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class E2ETimePerfExecution extends PerfExecution {

  private final Map<String, Long> regionsToUserPerf;

  public E2ETimePerfExecution(
      Set<String> configuration,
      Map<String, Long> regionsToPerf,
      Map<String, Long> regionsToUserPerf) {
    super(configuration, regionsToPerf);

    this.regionsToUserPerf = regionsToUserPerf;
  }

  // Dummy constructor for jackson xml
  private E2ETimePerfExecution() {
    super(new HashSet<>(), new HashMap<>());

    this.regionsToUserPerf = new HashMap<>();
  }

  public Map<String, Long> getRegionsToUserPerf() {
    return regionsToUserPerf;
  }
}
