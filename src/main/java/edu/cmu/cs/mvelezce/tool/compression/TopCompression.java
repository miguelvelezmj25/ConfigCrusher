package edu.cmu.cs.mvelezce.tool.compression;

import edu.cmu.cs.mvelezce.tool.Options;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.FileUtils;

public abstract class TopCompression<T> implements Compression<T> {

  public static final String DIRECTORY = Options.DIRECTORY + "/compression";

  private String programName;

  public TopCompression(String programName) {
    this.programName = programName;
  }

  @Override
  public T compressConfigurations(String[] args) throws IOException {
    Options.getCommandLine(args);

    String outputFile = this.getOutputDir();
    File file = new File(outputFile);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      Collection<File> files = FileUtils.listFiles(file, null, true);

      if (files.size() != 1) {
        throw new RuntimeException(
            "We expected to find 1 file in the directory, but that is not the case "
                + outputFile);
      }

      return this.readFromFile(files.iterator().next());
    }

    T configurationsToExecute = this.compressConfigurations();

    if (Options.checkIfSave()) {
      this.writeToFile(configurationsToExecute);
    }

    return configurationsToExecute;
  }

  public String getProgramName() {
    return this.programName;
  }

}
