package edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler;

import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.snapshot.Hotspot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RawJProfilerSamplingPerfExecution {

  private final Set<String> configuration;
  private final List<Hotspot> hotspots;

  // Dummy constructor for jackson xml
  private RawJProfilerSamplingPerfExecution() {
    this.configuration = new HashSet<>();
    this.hotspots = new ArrayList<>();
  }

  public RawJProfilerSamplingPerfExecution(Set<String> configuration, List<Hotspot> hotspots) {
    this.configuration = configuration;
    this.hotspots = hotspots;
  }

  public Set<String> getConfiguration() {
    return configuration;
  }

  public List<Hotspot> getHotspots() {
    return hotspots;
  }
}
