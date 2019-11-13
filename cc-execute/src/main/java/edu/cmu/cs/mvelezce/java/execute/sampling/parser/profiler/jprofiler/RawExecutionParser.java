package edu.cmu.cs.mvelezce.java.execute.sampling.parser.profiler.jprofiler;

import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class RawExecutionParser extends BaseRawExecutionParser<Object> {

  public RawExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  public void logExecution(Set<String> configuration, int iter) throws IOException {
    throw new UnsupportedOperationException("implement");
    //    List<String> trace = this.parseTrace();
    //    RawPerfExecution rawPerfExecution = new RawPerfExecution(configuration, trace);
    //
    //    String outputFile = this.getRawOutputDir(iter) + "/" + UUID.randomUUID() +
    // Options.DOT_JSON;
    //    File file = new File(outputFile);
    //    file.getParentFile().mkdirs();
    //
    //    ObjectMapper mapper = new ObjectMapper();
    //    mapper.writeValue(file, rawPerfExecution);
  }

  //  private String getRawOutputDir(int iter) {
  //    return this.outputDir + "/" + this.programName + "/execution/raw/" + iter;
  //  }
  //
  //  private List<String> parseTrace() throws IOException {
  //    File dirFile = new File(".");
  //    Collection<File> serializedFiles = FileUtils.listFiles(dirFile, new String[] {"ser"},
  // false);
  //
  //    if (serializedFiles.size() != 1) {
  //      throw new RuntimeException("The directory " + dirFile + " must have 1 file.");
  //    }
  //
  //    return this.deserialize(serializedFiles.iterator().next());
  //  }
  //
  //  private List<String> deserialize(File file) throws IOException {
  //    List<String> trace = new ArrayList<>();
  //
  //    BufferedReader reader = new BufferedReader(new FileReader(file));
  //    String line;
  //
  //    while ((line = reader.readLine()) != null) {
  //      trace.add(line);
  //    }
  //
  //    reader.close();
  //
  //    return trace;
  //  }

  public Map<Integer, Set<Object>> readResults() throws IOException {
    throw new UnsupportedOperationException("implement");
    //    Map<Integer, Set<RawPerfExecution>> itersToPerfExecutions = new HashMap<>();
    //
    //    int iter = 0;
    //    File file = new File(this.getRawOutputDir(iter));
    //
    //    while (file.exists()) {
    //      Set<RawPerfExecution> rawPerfExecutions = new HashSet<>();
    //      Collection<File> files = FileUtils.listFiles(file, new String[] {"json"}, true);
    //
    //      for (File perfFile : files) {
    //        RawPerfExecution rawPerfExecution = this.readFromFile(perfFile);
    //        rawPerfExecutions.add(rawPerfExecution);
    //      }
    //
    //      itersToPerfExecutions.put(iter, rawPerfExecutions);
    //
    //      iter++;
    //      file = new File(this.getRawOutputDir(iter));
    //    }
    //
    //    return itersToPerfExecutions;
  }

  //  private RawPerfExecution readFromFile(File file) throws IOException {
  //    ObjectMapper mapper = new ObjectMapper();
  //    return mapper.readValue(file, new TypeReference<RawPerfExecution>() {});
  //  }
}
