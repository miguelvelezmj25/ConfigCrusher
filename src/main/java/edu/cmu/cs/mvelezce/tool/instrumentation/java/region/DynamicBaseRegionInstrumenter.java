package edu.cmu.cs.mvelezce.tool.instrumentation.java.region;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
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

public abstract class DynamicBaseRegionInstrumenter
    extends BaseRegionInstrumenter<Set<Set<String>>> {

  public static final String DIRECTORY =
      Options.DIRECTORY + "/instrumentation/dynamic/java/programs";

  public DynamicBaseRegionInstrumenter(
      String programName,
      String classDir,
      Map<JavaRegion, Set<Set<String>>> regionsToInfluencingTaints) {
    super(programName, classDir, regionsToInfluencingTaints);
  }

  public DynamicBaseRegionInstrumenter(String programName) {
    this(programName, null, new HashMap<>());
  }

  // TODO MIGUEL this method is targeted to instrumenting with static information. Either save
  // all instrumentation in the same directory or make each instrumentator with static and dynamic
  // info override this method.
  @Override
  public void instrument(String[] args)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException,
          InterruptedException {
    Options.getCommandLine(args);

    File outputFile = new File(DIRECTORY + "/" + this.getProgramName());
    Options.checkIfDeleteResult(outputFile);

    if (outputFile.exists()) {
      Collection<File> files = FileUtils.listFiles(outputFile, new String[] {"json"}, false);

      if (files.size() != 1) {
        throw new RuntimeException(
            "We expected to find 1 file in the directory, but that is not the case " + outputFile);
      }

      Map<JavaRegion, Set<Set<String>>> regionsToData = this.readFromFile(files.iterator().next());
      setRegionsToData(regionsToData);

      return;
    }

    if (Options.checkIfDeleteResult()) {
      this.compile();
    }

    if (Options.checkIfSave()) {
      this.instrument();
      this.writeToFile(this.getRegionsToData());
    }
  }

  public void writeToFile(Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String outputFile =
        DIRECTORY + "/" + this.getProgramName() + "/" + this.getProgramName() + Options.DOT_JSON;
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
        newRegion =
            new JavaRegion.Builder(
                    oldRegion.getRegionID(),
                    oldRegion.getRegionPackage(),
                    oldRegion.getRegionClass(),
                    oldRegion.getRegionMethod())
                .startBytecodeIndex(oldRegion.getStartRegionIndex())
                .startBlockID(oldRegion.getStartMethodBlock().getID())
                .endBlocksIDs(endBlocksIDs)
                .build();
      } catch (NullPointerException npe) {
        continue;
      }

      RegionToInfo<Set<Set<String>>> regionToInfo = new RegionToInfo<>(newRegion, entry.getValue());
      decisionsAndOptions.add(regionToInfo);
    }

    mapper.writeValue(file, decisionsAndOptions);
  }

  public Map<JavaRegion, Set<Set<String>>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<RegionToInfo<Set<Set<String>>>> results =
        mapper.readValue(file, new TypeReference<List<RegionToInfo>>() {});
    Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();

    for (RegionToInfo<Set<Set<String>>> result : results) {
      regionsToOptionsSet.put(result.getRegion(), result.getInfo());
    }

    return regionsToOptionsSet;
  }
}
