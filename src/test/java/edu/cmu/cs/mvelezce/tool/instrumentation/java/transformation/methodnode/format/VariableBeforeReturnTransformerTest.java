package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.format;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.Formatter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class VariableBeforeReturnTransformerTest {

//    String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper-evaluation/original/running-example/src";
//    String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper-evaluation/original/running-example/target/classes";
//    String instrumentedClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper-evaluation/instrumented/running-example/src";
//    String instrumentedSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper-evaluation/instrumented/running-example/target/classes";

    @Test
    public void testRunningExample() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper-evaluation/instrumented/running-example/src";
        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper-evaluation/instrumented/running-example/target/classes";

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