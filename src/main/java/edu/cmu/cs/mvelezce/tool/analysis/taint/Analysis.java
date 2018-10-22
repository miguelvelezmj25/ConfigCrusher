package edu.cmu.cs.mvelezce.tool.analysis.taint;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

// TODO use generics for the ? extends region
public interface Analysis<T> {

  Map<JavaRegion, T> analyze() throws IOException;

  Map<JavaRegion, T> analyze(String[] args) throws IOException;

  void writeToFile(Map<JavaRegion, T> relevantRegionsToOptions) throws IOException;

  Map<JavaRegion, T> readFromFile(File file) throws IOException;

  String outputDir();

  Map<Region, Set<Set<String>>> transform(
      Map<? extends Region, Set<Set<String>>> regionsToOptionSet);

}
