package edu.cmu.cs.mvelezce.tool.instrumentation.java.region;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class DynamicBaseRegionInstrumenter extends
    BaseRegionInstrumenter<Set<Set<String>>> {

  public static final String DIRECTORY =
      Options.DIRECTORY + "/instrumentation/dynamic/java/programs";

  public DynamicBaseRegionInstrumenter(String programName, String classDir,
      Map<JavaRegion, Set<Set<String>>> regionsToInfluencingTaints) {
    super(programName, classDir, regionsToInfluencingTaints);
  }

  public void writeToFile(Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String outputFile = DIRECTORY + "/" + this.getProgramName() + "/" + this.getProgramName()
        + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    List<RegionToInfo> decisionsAndOptions = new ArrayList<>();

    for (Map.Entry<JavaRegion, Set<Set<String>>> entry : relevantRegionsToOptions.entrySet()) {
      JavaRegion oldRegion = entry.getKey();

      Set<String> endBlocksIDs = new HashSet<>();

      for (MethodBlock block : oldRegion.getEndMethodBlocks()) {
        endBlocksIDs.add(block.getID());
      }

      // TODO this catch has to be removed when we can handle methods with special cases
      JavaRegion newRegion;
      try {
        newRegion = new JavaRegion.Builder(oldRegion.getRegionID(),
            oldRegion.getRegionPackage(),
            oldRegion.getRegionClass(), oldRegion.getRegionMethod())
            .startBytecodeIndex(oldRegion.getStartRegionIndex())
            .startBlockID(oldRegion.getStartMethodBlock().getID()).endBlocksIDs(endBlocksIDs)
            .build();
      } catch (NullPointerException npe) {
        continue;
      }

      RegionToInfo<Set<Set<String>>> regionToInfo = new RegionToInfo<>(newRegion,
          entry.getValue());
      decisionsAndOptions.add(regionToInfo);
    }

    mapper.writeValue(file, decisionsAndOptions);
  }

  public Map<JavaRegion, Set<Set<String>>> readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("Implement");
  }

}
