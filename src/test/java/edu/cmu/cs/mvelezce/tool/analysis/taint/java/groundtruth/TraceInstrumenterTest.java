package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Utils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

@Deprecated
public class TraceInstrumenterTest {

//  @Test
//  public void phosphorExamples()
//      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
//    String programName = "phosphorExamples";
//    String pathToClasses = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
//
//    MethodTransformer transformer = new TraceInstrumenter(programName, pathToClasses);
//    transformer.transformMethods();
//  }
//
//  @Test
//  public void runningExample()
//      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
//    String programName = "phosphorExamples";
//    String pathToClasses = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
//    Set<String> classesToTransform = new HashSet<>();
//    classesToTransform
//        .add(Utils.getASMPackageAndClassName("edu/cmu/cs/mvelezce", "RunningExample"));
//
//    MethodTransformer transformer = new TraceInstrumenter(programName, pathToClasses,
//        classesToTransform, true);
//    transformer.transformMethods();
//  }
//
//  @Test
//  public void example4()
//      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
//    String programName = "example4";
//    String pathToClasses = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
//    Set<String> classesToTransform = new HashSet<>();
//    classesToTransform
//        .add(Utils.getASMPackageAndClassName("edu/cmu/cs/mvelezce/analysis", "Example4"));
//
//    MethodTransformer transformer = new TraceInstrumenter(programName, pathToClasses,
//        classesToTransform, true);
//    transformer.transformMethods();
//  }
//
//  @Test
//  public void orContext6()
//      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
//    String programName = "orContext6";
//    String pathToClasses = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
//    Set<String> classesToTransform = new HashSet<>();
//    classesToTransform
//        .add(Utils.getASMPackageAndClassName("edu/cmu/cs/mvelezce/analysis", "OrContext6"));
//
//    MethodTransformer transformer = new TraceInstrumenter(programName, pathToClasses,
//        classesToTransform, true);
//    transformer.transformMethods();
//  }

}