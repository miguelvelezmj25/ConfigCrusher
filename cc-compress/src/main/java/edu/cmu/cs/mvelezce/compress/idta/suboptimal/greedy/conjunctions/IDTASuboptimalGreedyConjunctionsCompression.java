package edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.compress.idta.IDTACompression;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.*;

/** Sub-optimal algorithm */
public class IDTASuboptimalGreedyConjunctionsCompression extends IDTACompression {

  private static final String OUTPUT_DIR =
      "../cc-compress/"
          + Options.DIRECTORY
          + "/compression/java/programs/idta/suboptimal/greedy/conjunctions";

  public IDTASuboptimalGreedyConjunctionsCompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  IDTASuboptimalGreedyConjunctionsCompression(
      String programName, List<String> options, Collection<Partitioning> allPartitions) {
    super(programName, options, allPartitions);
  }

  @Override
  public Set<Set<String>> analyze() {
    Set<Set<String>> configs = new HashSet<>();
    Set<Partition> partitions = this.expandAllPartitions();

    Set<Partition> coveredPartitions = new HashSet<>();

    while (coveredPartitions.size() != partitions.size()) {
      FeatureExpr newPartition = FeatureExprUtils.getTrue(IDTA.USE_BDD);

      for (Partition partition : partitions) {
        if (coveredPartitions.contains(partition)) {
          continue;
        }

        FeatureExpr andedFormula = newPartition.and(partition.getFeatureExpr());

        if (andedFormula.isContradiction()) {
          continue;
        }

        newPartition = andedFormula;
        coveredPartitions.add(partition);
      }

      Set<String> config = ConstraintUtils.toConfig(newPartition, this.getOptions());
      configs.add(config);
    }

    return configs;
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}
