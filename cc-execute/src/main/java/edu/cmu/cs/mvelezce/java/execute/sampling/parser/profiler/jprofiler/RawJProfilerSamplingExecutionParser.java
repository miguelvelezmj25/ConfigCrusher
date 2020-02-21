package edu.cmu.cs.mvelezce.java.execute.sampling.parser.profiler.jprofiler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.RawJProfilerSamplingPerfExecution;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.snapshot.Hotspot;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.snapshot.JProfilerSnapshotEntry;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.snapshot.Node;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.*;
import java.util.*;

public class RawJProfilerSamplingExecutionParser
    extends BaseRawExecutionParser<RawJProfilerSamplingPerfExecution> {

  public static final String ALL_THREAD_STATUS = "all";
  public static final String RUNNABLE_THREAD_STATUS = "running";

  private static final String JPROFILER_EXPORT_CMD_UBUNTU = "/home/miguel/jprofiler10/bin/jpexport";
  private static final String JPROFILER_EXPORT_CMD_OSX =
      "/Applications/JProfiler 10.app/Contents/Resources/app/bin/jpexport";
  private static final String JPROFILER_SNAPSHOT_NAME = "snapshot";
  private static final String JPROFILER_SNAPSHOT_FILE = JPROFILER_SNAPSHOT_NAME + ".jps";
  private static final String EXPORTED_FILE = JPROFILER_SNAPSHOT_NAME + ".xml";

  private static final String OPEN_NODE = "<node";
  private static final String OPEN_HOTSPOT = "<hotspot";
  private static final String CLOSE_NODE = "</node>";
  private static final String CLOSE_HOTSPOT = "</hotspot>";
  private static final String CLOSE_TAG = "/>";

  private final String threadStatus;

  public RawJProfilerSamplingExecutionParser(
      String programName, String outputDir, String threadStatus) {
    super(programName, outputDir);

    this.threadStatus = threadStatus;
  }

  public void logExecution(Set<String> configuration, int iter)
      throws IOException, InterruptedException {
    this.exportSnapShot();
    this.fixTreeLevelEntry();

    List<Hotspot> hotspots = this.parseHotpots();
    RawJProfilerSamplingPerfExecution rawJProfilerSamplingPerfExecution =
        new RawJProfilerSamplingPerfExecution(configuration, hotspots);

    String outputFile = this.getRawOutputDir(iter) + "/" + UUID.randomUUID() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, rawJProfilerSamplingPerfExecution);
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
    writer.flush();
    writer.close();
  }

  private List<Hotspot> parseHotpots() throws IOException {
    List<Hotspot> hotspots = new ArrayList<>(1_000);

    XmlMapper xmlMapper = new XmlMapper();
    BufferedReader reader = new BufferedReader(new FileReader(EXPORTED_FILE));
    String line;
    Deque<JProfilerSnapshotEntry> stack = new ArrayDeque<>(10_000);

    while ((line = reader.readLine()) != null) {
      if (line.contains(OPEN_HOTSPOT)) {
        Hotspot hotspot =
            xmlMapper.readValue(line + CLOSE_HOTSPOT, new TypeReference<Hotspot>() {});
        stack.push(hotspot);
        hotspots.add(hotspot);

        if (line.contains(CLOSE_TAG)) {
          stack.pop();
        }
      } else if (line.contains(OPEN_NODE)) {
        Node node = xmlMapper.readValue(line + CLOSE_NODE, new TypeReference<Node>() {});
        JProfilerSnapshotEntry top = stack.peek();

        if (top == null) {
          throw new RuntimeException("The stack of nodes and hotpots is empty");
        }

        top.getNodes().add(node);
        stack.push(node);

        if (line.contains(CLOSE_TAG)) {
          stack.pop();
        }
      } else if (line.contains(CLOSE_NODE)) {
        if (stack.isEmpty()) {
          throw new RuntimeException("The stack of nodes and hotpots is empty");
        }

        if (!(stack.peek() instanceof Node)) {
          throw new RuntimeException(
              "Expected to pop a Node, but popped a " + stack.peek().getClass());
        }

        stack.pop();
      } else if (line.contains(CLOSE_HOTSPOT)) {
        if (stack.isEmpty()) {
          throw new RuntimeException("The stack of nodes and hotpots is empty");
        }

        if (!(stack.peek() instanceof Hotspot)) {
          throw new RuntimeException(
              "Expected to pop a Hotspot, but popped a " + stack.peek().getClass());
        }

        stack.pop();
      }
    }

    reader.close();

    return hotspots;
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
    commandList.add(this.getExportCmd());
    commandList.add(JPROFILER_SNAPSHOT_FILE);
    commandList.add("HotSpots");
    commandList.add("-expandbacktraces=true");
    commandList.add("-threadstatus=" + this.threadStatus);
    commandList.add("-valuesummation=self");
    commandList.add(JPROFILER_SNAPSHOT_NAME + ".xml");

    return commandList;
  }

  private String getExportCmd() {
    String os = IDTAJProfilerSamplingExecutor.getOS();

    switch (os) {
      case IDTAJProfilerSamplingExecutor.LINUX:
        return JPROFILER_EXPORT_CMD_UBUNTU;
      case IDTAJProfilerSamplingExecutor.MAC_OS_X:
        return JPROFILER_EXPORT_CMD_OSX;
      default:
        throw new RuntimeException("Do not have an agent path for " + os);
    }
  }

  @Override
  public RawJProfilerSamplingPerfExecution readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(file, new TypeReference<RawJProfilerSamplingPerfExecution>() {});
  }
}
