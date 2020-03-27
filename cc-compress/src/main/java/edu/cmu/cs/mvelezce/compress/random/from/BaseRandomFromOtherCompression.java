package edu.cmu.cs.mvelezce.compress.random.from;

import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.*;

public abstract class BaseRandomFromOtherCompression extends BaseCompression {

  private static final String OUTPUT_DIR =
      "../cc-compress/" + Options.DIRECTORY + "/compression/java/programs/random/from";

  private final List<Set<String>> otherConfigs;
  private final Random random = new Random();

  public BaseRandomFromOtherCompression(
      String programName, List<String> options, Set<Set<String>> otherConfigs) {
    super(programName, options);

    this.otherConfigs = new ArrayList<>(otherConfigs);
  }

  public static String getOutputDir() {
    return OUTPUT_DIR;
  }

  public Set<Set<String>> getRandomConfigsFromOther(int size) {
    Set<Set<String>> randomConfigs = new HashSet<>();

    while (randomConfigs.size() != size) {
      int index = this.random.nextInt(this.otherConfigs.size());

      randomConfigs.add(this.otherConfigs.get(index));
    }

    return randomConfigs;
  }
}
