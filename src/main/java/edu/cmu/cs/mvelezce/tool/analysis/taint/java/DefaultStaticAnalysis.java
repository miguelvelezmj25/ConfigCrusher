package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class DefaultStaticAnalysis extends BaseStaticAnalysis {

    public DefaultStaticAnalysis(String programName) {
        super(programName);
    }

    public DefaultStaticAnalysis() {
        this(null);
    }

    @Override
    public Map<JavaRegion, Set<Set<String>>> analyze() throws IOException {
        throw new UnsupportedOperationException("This class does not implement analyze");
    }
}
