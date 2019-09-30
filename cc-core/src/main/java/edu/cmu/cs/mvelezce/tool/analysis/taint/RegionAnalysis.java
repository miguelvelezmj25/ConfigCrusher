package edu.cmu.cs.mvelezce.tool.analysis.taint;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

// TODO use generics for the ? extends region
public interface RegionAnalysis<S> extends Analysis<Map<JavaRegion, S>> {

  Map<JavaRegion, S> analyze() throws Exception;

  Map<JavaRegion, S> analyze(String[] args) throws Exception;

  void writeToFile(Map<JavaRegion, S> relevantRegionsToOptions) throws IOException;

  Map<JavaRegion, S> readFromFile(File file) throws IOException;

  String outputDir();

  // TODO is this method needed in all region analyses?
  Map<Region, Set<Set<String>>> transform(
      Map<? extends Region, Set<Set<String>>> regionsToOptionSet);

}
