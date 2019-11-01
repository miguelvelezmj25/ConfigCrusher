package edu.cmu.cs.mvelezce.java.idta;

import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.java.Executor;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class IDTAExecutorTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Set<Set<String>> configurations = new HashSet<>();
    Set<String> x = new HashSet<>();
    x.add("A");
    x.add("B");
    configurations.add(x);

    Executor executor = new IDTAExecutor(programName, configurations);

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i2";

    executor.execute(args);
    System.out.println();
  }
}
