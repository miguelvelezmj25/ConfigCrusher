package edu.cmu.cs.mvelezce.tool.compression;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.SinkData;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.io.FileUtils;

// TODO make a base class for both static and dynamic results
public abstract class BaseDynamicCompression implements Compression {

  private final String programName;
  private final Set<String> options;
  private final Collection<SinkData> constraints;

  protected BaseDynamicCompression(String programName, Set<String> options,
      Collection<SinkData> constraints) {
    this.programName = programName;
    this.options = options;
    this.constraints = constraints;
  }

  @Override
  public Set<Set<String>> compressConfigurations(String[] args) throws IOException {
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

    Set<Set<String>> configurationsToExecute = this.compressConfigurations();

    if (Options.checkIfSave()) {
      this.writeToFile(configurationsToExecute);
    }

    return configurationsToExecute;
  }

  @Override
  public void writeToFile(Set<Set<String>> compressedConfigurations) throws IOException {
    String outputFile = this.getOutputDir() + "/" + this.programName + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, compressedConfigurations);
  }

  @Override
  public Set<Set<String>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<Set<Set<String>>>() {
    });
  }

  @Override
  public String getOutputDir() {
    return Options.DIRECTORY + "/compression/dynamic/java/programs/" + this.programName;
  }

  protected String getProgramName() {
    return programName;
  }

  protected Collection<SinkData> getConstraints() {
    return constraints;
  }

  protected Set<String> getOptions() {
    return options;
  }
}
