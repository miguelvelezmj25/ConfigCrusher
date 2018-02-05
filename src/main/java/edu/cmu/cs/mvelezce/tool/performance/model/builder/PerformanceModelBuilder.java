package edu.cmu.cs.mvelezce.tool.performance.model.builder;

import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;

import java.io.File;
import java.io.IOException;

public interface PerformanceModelBuilder {

    public PerformanceModel createModel();

    public PerformanceModel createModel(String[] args) throws IOException;

    public void writeToFile(PerformanceModel performanceModel) throws IOException;

    public PerformanceModel readFromFile(File file) throws IOException;

    public String getOutputDir();
}
