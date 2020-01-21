package edu.cmu.cs.mvelezce.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.results.RegionConstraintPretty;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class RegionsWithConstraintsUtils {

  private RegionsWithConstraintsUtils() {}

  public static void writeToFile(
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      String outputFile,
      Collection<String> options)
      throws IOException {
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    Set<RegionConstraintPretty> regionsToPrettyConstraints = new HashSet<>();

    for (Map.Entry<JavaRegion, Set<FeatureExpr>> entry : regionsToData.entrySet()) {
      Set<String> prettyConstraints = new HashSet<>();
      Set<FeatureExpr> constraints = entry.getValue();

      for (FeatureExpr constraint : constraints) {
        String prettyConstraint = ConstraintUtils.prettyPrintFeatureExpr(constraint, options);
        prettyConstraints.add(prettyConstraint);
      }

      JavaRegion region = entry.getKey();
      Set<String> endBlocks = new HashSet<>();

      for (MethodBlock endBlock : region.getEndMethodBlocks()) {
        endBlocks.add(endBlock.getID());
      }

      RegionConstraintPretty regionToPrettyConstraints =
          new RegionConstraintPretty(
              region.getRegionPackage(),
              region.getRegionClass(),
              region.getRegionMethodSignature(),
              region.getStartIndex(),
              prettyConstraints,
              region.getId().toString(),
              region.getStartMethodBlock().getID(),
              endBlocks);

      regionsToPrettyConstraints.add(regionToPrettyConstraints);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, regionsToPrettyConstraints);
  }

  public static Map<JavaRegion, Set<FeatureExpr>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Set<RegionConstraintPretty> regionsConstraintPretty =
        mapper.readValue(file, new TypeReference<Set<RegionConstraintPretty>>() {});

    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = new HashMap<>();

    for (RegionConstraintPretty regionConstraintPretty : regionsConstraintPretty) {
      JavaRegion region =
          new JavaRegion.Builder(
                  UUID.fromString(regionConstraintPretty.getId()),
                  regionConstraintPretty.getPackageName(),
                  regionConstraintPretty.getClassName(),
                  regionConstraintPretty.getMethodSignature())
              .build();

      Set<FeatureExpr> constraints = getConstraints(regionConstraintPretty.getInfo());

      regionsToConstraints.put(region, constraints);
    }

    return regionsToConstraints;
  }

  private static Set<FeatureExpr> getConstraints(Set<String> prettyConstraints) {
    Set<FeatureExpr> constraints = new HashSet<>();

    for (String prettyConstraint : prettyConstraints) {
      FeatureExpr constraint = MinConfigsGenerator.parseAsFeatureExpr(prettyConstraint);
      constraints.add(constraint);
    }

    return constraints;
  }
}
