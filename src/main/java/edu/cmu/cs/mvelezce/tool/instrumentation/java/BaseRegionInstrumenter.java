package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.DecisionToInfo;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public abstract class BaseRegionInstrumenter extends BaseInstrumenter {

  public static final String DIRECTORY = Options.DIRECTORY + "/instrumentation/java/programs";

  private Map<JavaRegion, Set<Set<String>>> regionsToOptionSet;

  public BaseRegionInstrumenter(String programName, String classDir,
      Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) {
    super(programName, null, classDir);
    this.regionsToOptionSet = regionsToOptionSet;
  }

  public BaseRegionInstrumenter(String programName) {
    this(programName, null, new HashMap<>());
  }

  @Override
  public void instrument(String[] args)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException, InterruptedException {
    Options.getCommandLine(args);

    File outputFile = new File(BaseRegionInstrumenter.DIRECTORY + "/" + this.getProgramName());
    Options.checkIfDeleteResult(outputFile);

    if (outputFile.exists()) {
      Collection<File> files = FileUtils.listFiles(outputFile, new String[]{"json"}, false);

      if (files.size() != 1) {
        throw new RuntimeException(
            "We expected to find 1 file in the directory, but that is not the case "
                + outputFile);
      }

      this.regionsToOptionSet = this.readFromFile(files.iterator().next());

      return;
    }

    if (Options.checkIfDeleteResult()) {
      this.compile();
    }

    if (Options.checkIfSave()) {
      this.instrument();
      this.writeToFile(this.regionsToOptionSet);
    }
  }

  public void writeToFile(Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String outputFile =
        BaseRegionInstrumenter.DIRECTORY + "/" + this.getProgramName() + "/" + this.getProgramName()
            + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    List<DecisionToInfo> decisionsAndOptions = new ArrayList<>();

    for (Map.Entry<JavaRegion, Set<Set<String>>> regionToOptionsSet : relevantRegionsToOptions
        .entrySet()) {
      JavaRegion oldRegion = regionToOptionsSet.getKey();

      Set<String> endBlocksIDs = new HashSet<>();

      for (MethodBlock block : oldRegion.getEndMethodBlocks()) {
        endBlocksIDs.add(block.getID());
      }

      JavaRegion newRegion = new JavaRegion.Builder(oldRegion.getRegionID(),
          oldRegion.getRegionPackage(),
          oldRegion.getRegionClass(), oldRegion.getRegionMethod())
          .startBytecodeIndex(oldRegion.getStartBytecodeIndex())
          .startBlockID(oldRegion.getStartMethodBlock().getID()).endBlocksIDs(endBlocksIDs)
          .build();
      DecisionToInfo<Set<Set<String>>> decisionToInfo = new DecisionToInfo<>(newRegion,
          regionToOptionsSet.getValue());
      decisionsAndOptions.add(decisionToInfo);
    }

    mapper.writeValue(file, decisionsAndOptions);
  }

  public Map<JavaRegion, Set<Set<String>>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<DecisionToInfo<Set<Set<String>>>> results = mapper
        .readValue(file, new TypeReference<List<DecisionToInfo>>() {
        });
    Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();

    for (DecisionToInfo<Set<Set<String>>> result : results) {
      regionsToOptionsSet.put(result.getRegion(), result.getInfo());
    }

    return regionsToOptionsSet;
  }


  public Map<JavaRegion, Set<Set<String>>> getRegionsToOptionSet() {
    return this.regionsToOptionSet;
  }
}
