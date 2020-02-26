package edu.cmu.cs.mvelezce.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.idta.partition.TotalPartition;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.results.RegionPartitionPretty;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class RegionsWithPartitionsUtils {

  private RegionsWithPartitionsUtils() {}

  public static void writeToFile(
      Map<JavaRegion, Partitioning> regionsToData, String outputFile, Collection<String> options)
      throws IOException {
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    Set<RegionPartitionPretty> regionsToPrettyPartitions = new HashSet<>();

    for (Map.Entry<JavaRegion, Partitioning> entry : regionsToData.entrySet()) {
      Set<String> prettyPartitions = new HashSet<>();
      Partitioning partitions = entry.getValue();

      for (Partition partition : partitions.getPartitions()) {
        String prettyPartition =
            ConstraintUtils.prettyPrintFeatureExpr(partition.getFeatureExpr(), options);
        prettyPartitions.add(prettyPartition);
      }

      JavaRegion region = entry.getKey();
      Set<String> endBlocks = new HashSet<>();

      for (MethodBlock endBlock : region.getEndMethodBlocks()) {
        endBlocks.add(endBlock.getID());
      }

      RegionPartitionPretty regionToPrettyPartitions =
          new RegionPartitionPretty(
              region.getRegionPackage(),
              region.getRegionClass(),
              region.getRegionMethodSignature(),
              region.getStartIndex(),
              prettyPartitions,
              region.getId().toString(),
              region.getStartMethodBlock().getID(),
              endBlocks);

      regionsToPrettyPartitions.add(regionToPrettyPartitions);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, regionsToPrettyPartitions);
  }

  public static Map<JavaRegion, Partitioning> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Set<RegionPartitionPretty> regionsPartitionPretty =
        mapper.readValue(file, new TypeReference<Set<RegionPartitionPretty>>() {});

    Map<JavaRegion, Partitioning> regionsToPartitions = new HashMap<>();

    for (RegionPartitionPretty regionPartitionPretty : regionsPartitionPretty) {
      JavaRegion region =
          new JavaRegion.Builder(
                  UUID.fromString(regionPartitionPretty.getId()),
                  regionPartitionPretty.getPackageName(),
                  regionPartitionPretty.getClassName(),
                  regionPartitionPretty.getMethodSignature())
              .build();

      Partitioning partitions = getPartitions(regionPartitionPretty.getInfo());

      regionsToPartitions.put(region, partitions);
    }

    return regionsToPartitions;
  }

  private static Partitioning getPartitions(Set<String> prettyPartitions) {
    Set<Partition> partitions = new HashSet<>();

    for (String prettyPartition : prettyPartitions) {
      Partition partition =
          new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, prettyPartition));
      partitions.add(partition);
    }

    return new TotalPartition(partitions);
  }
}
