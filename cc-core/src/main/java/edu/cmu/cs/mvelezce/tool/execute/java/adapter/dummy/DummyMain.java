package edu.cmu.cs.mvelezce.tool.execute.java.adapter.dummy;

import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class DummyMain extends BaseMain {

  static final String DUMMY_MAIN = DummyMain.class.getCanonicalName();

  private DummyMain(String programName, String iteration, String[] args) {
    super(programName, iteration, args);
  }

  public static void main(String[] args) {
    throw new UnsupportedOperationException("Should not execute");
  }

  @Override
  public void logExecution() throws IOException {
    throw new UnsupportedOperationException("Should not execute");
  }

  @Override
  public void execute(String mainClass, String[] args) {
    throw new UnsupportedOperationException("Should not execute");
  }
}
