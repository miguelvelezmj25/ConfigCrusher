package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;

public class ControlFlowDecisionInstrumenterTest {

  @Test
  public void runningExample()
      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
    String pathToClasses = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
    MethodTransformer transformer = new ControlFlowDecisionInstrumenter(pathToClasses);
    transformer.transformMethods();
  }
}