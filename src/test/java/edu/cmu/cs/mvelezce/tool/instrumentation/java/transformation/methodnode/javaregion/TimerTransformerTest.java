package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.StaticAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow.TaintFlowAnalysis;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class TimerTransformerTest {

    @Test
    public void testRunningExample() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "running-example";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper-evaluation/original/running-example/target/classes";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        MethodTransformer methodTransformer = new TimerTransformer(classDirectory, decisionsToOptions.keySet());
        methodTransformer.transformMethods();
    }
}