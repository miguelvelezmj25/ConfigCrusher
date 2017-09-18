package edu.cmu.cs.mvelezce.tool;

import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceModel;
import org.junit.Test;

public class DefaultTest {

    @Test
    public void run() throws Exception {
        String programName = "running-example";
        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example";
        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/target/classes";
        String entry = "edu.cmu.cs.mvelezce.Example";

        String[] args = new String[0];

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Default approach = new Default(programName, srcDir, classDir, entry);
        approach.format();
        PerformanceModel performanceModel = approach.run(args);

        performanceModel.toString();
    }

}