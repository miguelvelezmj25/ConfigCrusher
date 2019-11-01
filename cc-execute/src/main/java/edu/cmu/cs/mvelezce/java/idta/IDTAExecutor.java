package edu.cmu.cs.mvelezce.java.idta;

import edu.cmu.cs.mvelezce.java.BaseExecutor;
import edu.cmu.cs.mvelezce.java.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.adapters.trivial.TrivialExecutorAdapter;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.IOException;
import java.util.Set;

public class IDTAExecutor extends BaseExecutor {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/executor/java/idta/programs";

  IDTAExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations);
  }

  @Override
  public Object executeIteration(int iteration) throws InterruptedException, IOException {

    ExecutorAdapter adapter;

    switch (this.getProgramName()) {
      case TrivialExecutorAdapter.PROGRAM_NAME:
        adapter = new TrivialExecutorAdapter(this);
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + this.getProgramName());
    }

    for (Set<String> configuration : this.getConfigurations()) {
      adapter.execute(configuration);
      adapter.logExecution(configuration, iteration);

      System.gc();
      Thread.sleep(5000);
    }

    throw new UnsupportedOperationException("Continue, whatever continue means");
    //    String outputDir = this.getOutputDir() + "/" + this.getProgramName() + "/" + iteration;
    //    File outputFile = new File(outputDir);
    //
    //    if (!outputFile.exists()) {
    //      throw new RuntimeException("The output file could not be found " + outputDir);
    //    }
    //
    //    return this.aggregateExecutions(outputFile);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + this.getProgramName();
  }
}
