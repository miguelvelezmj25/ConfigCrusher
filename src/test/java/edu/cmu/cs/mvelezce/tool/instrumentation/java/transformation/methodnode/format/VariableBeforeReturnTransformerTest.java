package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.format;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.Formatter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class VariableBeforeReturnTransformerTest {

//    String originalClassDirectory = System.getProperty("user.home") + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/src";
//    String originalSrcDirectory = System.getProperty("user.home") + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/target/classes";
//    String instrumentedClassDirectory = System.getProperty("user.home") + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/src";
//    String instrumentedSrcDirectory = System.getProperty("user.home") + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/target/classes";

    @Test
    public void testRunningExample() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String srcDir = System.getProperty("user.home") + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/src";
        String classDir = System.getProperty("user.home") + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/target/classes";

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter formatter = new Formatter(srcDir, classDir);
        formatter.instrument(args);
    }

}