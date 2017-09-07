package edu.cmu.cs.mvelezce.tool.analysis.taint;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;

import java.util.Map;
import java.util.Set;

// TODO use generics for the ? extends region
public interface Analysis {

    public Map<Region, Set<Set<String>>> transform(Map<? extends Region, Set<Set<String>>> regionsToOptionSet);

}
