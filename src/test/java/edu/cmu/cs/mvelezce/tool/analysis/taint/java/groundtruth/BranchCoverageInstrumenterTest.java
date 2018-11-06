package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Utils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class BranchCoverageInstrumenterTest {

  @Test
  public void runningExample()
      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
    String pathToClasses = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
    Set<String> classesToTransform = new HashSet<>();
    classesToTransform
        .add(Utils.getASMPackageAndClassName("edu/cmu/cs/mvelezce", "RunningExample"));

    MethodTransformer transformer = new BranchCoverageInstrumenter(pathToClasses,
        classesToTransform);
    transformer.transformMethods();
  }

  @Test
  public void simpleExample1()
      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
    String pathToClasses = SimpleExample1Adapter.ORIGINAL_CLASS_PATH;
    Set<String> classesToTransform = new HashSet<>();
    classesToTransform
        .add(Utils.getASMPackageAndClassName("edu/cmu/cs/mvelezce/analysis", "SimpleExample1"));

    MethodTransformer transformer = new BranchCoverageInstrumenter(pathToClasses,
        classesToTransform);
    transformer.transformMethods();
  }

  @Test
  public void phosphorExamples()
      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
    String pathToClasses = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";

    MethodTransformer transformer = new BranchCoverageInstrumenter(pathToClasses);
    transformer.transformMethods();
  }
}