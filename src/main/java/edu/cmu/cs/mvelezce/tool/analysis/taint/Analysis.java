package edu.cmu.cs.mvelezce.tool.analysis.taint;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

// TODO use generics for the ? extends region
public interface Analysis {

  Map<JavaRegion, Set<Set<String>>> analyze() throws IOException;

  Map<JavaRegion, Set<Set<String>>> analyze(String[] args) throws IOException;

  void writeToFile(Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions)
      throws IOException;

  Map<JavaRegion, Set<Set<String>>> readFromFile(File file) throws IOException;

  Map<Region, Set<Set<String>>> transform(
      Map<? extends Region, Set<Set<String>>> regionsToOptionSet);

}
