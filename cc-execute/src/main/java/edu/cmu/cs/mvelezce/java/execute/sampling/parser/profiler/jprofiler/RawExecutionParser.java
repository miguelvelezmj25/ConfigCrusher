package edu.cmu.cs.mvelezce.java.execute.sampling.parser.profiler.jprofiler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.Hotspot;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.RawPerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RawExecutionParser extends BaseRawExecutionParser<RawPerfExecution> {

  private static final String JPROFILER_EXPORT_CMD =
      "/Applications/JProfiler 10.app/Contents/Resources/app/bin/jpexport";
  private static final String JPROFILER_SNAPSHOT_NAME = "snapshot";
  private static final String JPROFILER_SNAPSHOT_FILE = JPROFILER_SNAPSHOT_NAME + ".jps";
  private static final String EXPORTED_FILE = JPROFILER_SNAPSHOT_NAME + ".xml";

  public RawExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  public void logExecution(Set<String> configuration, int iter)
      throws IOException, InterruptedException {
    this.exportSnapShot();
    this.fixTreeLevelEntry();

    List<Hotspot> hotspots = this.parseHotpots();
    RawPerfExecution rawPerfExecution = new RawPerfExecution(configuration, hotspots);

    String outputFile = this.getRawOutputDir(iter) + "/" + UUID.randomUUID() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, rawPerfExecution);
  }

  private void fixTreeLevelEntry() throws IOException {
    String tmpFileName = "tmp.xml";
    this.writeToTmpFile(tmpFileName);
    this.renameTmpFile(tmpFileName);
  }

  private void renameTmpFile(String tmpFileName) {
    File oldFile = new File(EXPORTED_FILE);
    oldFile.delete();

    File newFile = new File(tmpFileName);
    newFile.renameTo(oldFile);
  }

  private void writeToTmpFile(String tmpFileName) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(EXPORTED_FILE));
    BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFileName));
    String line;

    while ((line = reader.readLine()) != null) {
      if (line.contains("<tree ")) {
        line = "<tree>";
      }

      writer.write(line + "\n");
    }

    reader.close();
    writer.close();
  }

  private List<Hotspot> parseHotpots() throws IOException {
    XmlMapper xmlMapper = new XmlMapper();

    return xmlMapper.readValue(new File(EXPORTED_FILE), new TypeReference<List<Hotspot>>() {});
  }

  private void exportSnapShot() throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.getCommandList();
    builder.command(commandList);

    Process process = builder.start();

    Executor.processOutput(process);
    Executor.processError(process);

    process.waitFor();
  }

  private List<String> getCommandList() {
    List<String> commandList = new ArrayList<>();

    new ArrayList<>();
    commandList.add(JPROFILER_EXPORT_CMD);
    commandList.add(JPROFILER_SNAPSHOT_FILE);
    commandList.add("HotSpots");
    commandList.add("-expandbacktraces=true");
    commandList.add("-threadstatus=all");
    commandList.add(JPROFILER_SNAPSHOT_NAME + ".xml");

    return commandList;
  }

  @Override
  public RawPerfExecution readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(file, new TypeReference<RawPerfExecution>() {});
  }
}
