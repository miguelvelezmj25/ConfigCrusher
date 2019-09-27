package edu.cmu.cs.mvelezce.tool.instrumentation.java.region;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class StaticBaseRegionInstrumenter extends BaseRegionInstrumenter<Set<Set<String>>> {

  public static final String DIRECTORY = Options.DIRECTORY + "/instrumentation/java/programs";

  public StaticBaseRegionInstrumenter(String programName, String classDir,
      Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) {
    super(programName, classDir, regionsToOptionSet);
  }

  public StaticBaseRegionInstrumenter(String programName) {
    this(programName, null, new HashMap<>());
  }

  public void writeToFile(Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String outputFile =
        StaticBaseRegionInstrumenter.DIRECTORY + "/" + this.getProgramName() + "/" + this.getProgramName()
            + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    List<RegionToInfo> decisionsAndOptions = new ArrayList<>();

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
          .startBytecodeIndex(oldRegion.getStartRegionIndex())
          .startBlockID(oldRegion.getStartMethodBlock().getID()).endBlocksIDs(endBlocksIDs)
          .build();
      RegionToInfo<Set<Set<String>>> regionToInfo = new RegionToInfo<>(newRegion,
          regionToOptionsSet.getValue());
      decisionsAndOptions.add(regionToInfo);
    }

    mapper.writeValue(file, decisionsAndOptions);
  }

  public Map<JavaRegion, Set<Set<String>>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<RegionToInfo<Set<Set<String>>>> results = mapper
        .readValue(file, new TypeReference<List<RegionToInfo>>() {
        });
    Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();

    for (RegionToInfo<Set<Set<String>>> result : results) {
      regionsToOptionsSet.put(result.getRegion(), result.getInfo());
    }

    return regionsToOptionsSet;
  }

}
