package edu.cmu.cs.mvelezce.analysis;

import edu.cmu.cs.mvelezce.utils.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public abstract class BaseAnalysis<T> implements Analysis<T> {

  private final String programName;

  public BaseAnalysis(String programName) {
    this.programName = programName;
  }

  @Override
  public T analyze(String[] args) throws IOException, InterruptedException {
    Options.getCommandLine(args);

    String outputFile = this.outputDir();
    File file = new File(outputFile);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      Collection<File> files = FileUtils.listFiles(file, null, true);

      if (files.size() != 1) {
        throw new RuntimeException(
            "We expected to find 1 file in the directory, but that is not the case " + outputFile);
      }

      return this.readFromFile(files.iterator().next());
    }

    T analysisResults = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(analysisResults);
    }

    return analysisResults;
  }

  public String getProgramName() {
    return this.programName;
  }
}
