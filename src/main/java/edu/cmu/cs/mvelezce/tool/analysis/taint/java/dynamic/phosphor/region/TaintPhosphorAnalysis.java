package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.region;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicRegionAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorControlFlowInfo;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.PhosphorTaintAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaintPhosphorAnalysis extends BaseDynamicRegionAnalysis<InfluencingTaints> {

  private final PhosphorTaintAnalysis phosphorTaintAnalysis;

  public TaintPhosphorAnalysis(String programName) {
    super(programName, new HashSet<>(), new HashSet<>());

    this.phosphorTaintAnalysis = new PhosphorTaintAnalysis(programName, new ArrayList<>());
  }

  @Override
  public Map<JavaRegion, InfluencingTaints> analyze() throws IOException {
    Set<PhosphorControlFlowInfo> results = this.readPhosphorTaintResults();
    Map<JavaRegion, InfluencingTaints> regionsToOptionsSet = new HashMap<>();

    for (PhosphorControlFlowInfo result : results) {
      JavaRegion region = new JavaRegion.Builder(result.getPackageName(), result.getClassName(),
          result.getMethodSignature()).startBytecodeIndex(result.getDecisionIndex()).build();

      regionsToOptionsSet.put(region, result.getInfluencingTaints());
    }

    return regionsToOptionsSet;
  }

  @Override
  public Map<JavaRegion, InfluencingTaints> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<RegionToInfo<InfluencingTaints>> results = mapper
        .readValue(file, new TypeReference<List<RegionToInfo<InfluencingTaints>>>() {
        });
    Map<JavaRegion, InfluencingTaints> regionsToOptionsSet = new HashMap<>();

    for (RegionToInfo<InfluencingTaints> result : results) {
      regionsToOptionsSet.put(result.getRegion(), result.getInfo());
    }

    return regionsToOptionsSet;
  }

  @Override
  public String outputDir() {
    return BaseDynamicRegionAnalysis.DIRECTORY + "/" + this.getProgramName() + "/analysis";
  }

  private Set<PhosphorControlFlowInfo> readPhosphorTaintResults() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    File inputFile = new File(
        this.phosphorTaintAnalysis.outputDir() + "/" + this.getProgramName() + ".json");

    return mapper
        .readValue(inputFile, new TypeReference<Set<PhosphorControlFlowInfo>>() {
        });
  }

}
