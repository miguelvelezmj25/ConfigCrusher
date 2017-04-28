package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by mvelezce on 4/28/17.
 */
public class ProgramAnalysisTest {

    @Test
    public void testAnalyse() throws Exception {
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep1.FILENAME);

        Map<JavaRegion, Set<String>> output = ProgramAnalysis.analyse(Sleep1.CLASS, Sleep1.FILENAME, programFiles);

        System.out.println(output);
    }

}