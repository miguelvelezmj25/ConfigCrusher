package edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.export;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.adapters.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.Hotspot;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.JProfilerHotspot;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public abstract class BaseExportSnapshot implements Analysis<Map<Integer, Set<Hotspot>>> {

  private static final String JPROFILER_EXPORT_CMD =
      "/Applications/JProfiler 10.app/Contents/Resources/app/bin/jpexport";

  private final String programName;
  private final String outputDir;

  public BaseExportSnapshot(String programName) {
    this.programName = programName;

    this.outputDir = this.outputDir() + "/" + programName + "/profiler/processed";
  }

  @Override
  public Map<Integer, Set<Hotspot>> analyze() throws IOException, InterruptedException {
    File workingDir = this.getWorkingDir();
    Collection<File> files = FileUtils.listFiles(workingDir, new String[] {"jps"}, false);

    for (File file : files) {
      this.exportSnapshot(workingDir, file.getName());
      this.getHotSpot();
    }

    throw new UnsupportedOperationException("Implement");
  }

  private void getHotSpot() throws IOException {
    XmlMapper xmlMapper = new XmlMapper();

    BufferedReader reader =
        new BufferedReader(
            new FileReader(
                "/Users/mvelezce/Documents/programming/java/projects/performance-mapper-evaluation/original/berkeley-db/berkeley-db.0.jps.xml"));
    String line;

    while ((line = reader.readLine()) != null) {
      line = line.trim();

      if (!line.startsWith("<hotspot")) {
        continue;
      }

      line = line.replaceAll(">", "/>");

      JProfilerHotspot profilerHotspot =
          xmlMapper.readValue(line, new TypeReference<JProfilerHotspot>() {});

      throw new UnsupportedOperationException("Implement");
    }

    reader.close();

    System.out.println();
  }

  private void exportSnapshot(File workingDir, String snapshot)
      throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = new ArrayList<>();
    commandList.add(JPROFILER_EXPORT_CMD);
    commandList.add(snapshot);
    commandList.add("HotSpots");
    commandList.add("-expandbacktraces=true");
    commandList.add(snapshot + ".xml");
    builder.command(commandList);
    builder.directory(workingDir);

    Process process = builder.start();

    Executor.processOutput(process);
    Executor.processError(process);

    process.waitFor();
  }

  private File getWorkingDir() {
    String dir;

    switch (this.programName) {
      case MeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        dir = "../" + MeasureDiskOrderedScanAdapter.ORIGINAL_ROOT_DIR;
        break;
      default:
        throw new RuntimeException("Could not find the working directory for " + this.programName);
    }

    return new File(dir);
  }

  @Override
  public Map<Integer, Set<Hotspot>> analyze(String[] args)
      throws IOException, InterruptedException {
    Options.getCommandLine(args);

    File file = new File(this.outputDir);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      return this.readFromFile(file);
    }

    Map<Integer, Set<Hotspot>> results = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(results);
    }

    return results;
  }

  @Override
  public void writeToFile(Map<Integer, Set<Hotspot>> results) throws IOException {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  public Map<Integer, Set<Hotspot>> readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("Implement");
  }
}
