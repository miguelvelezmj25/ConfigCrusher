package edu.cmu.cs.mvelezce.adapter.adapters;

import java.util.Set;

public interface Adapter {

  //  void execute(Set<String> configuration, int iteration) throws IOException,
  // InterruptedException;
  //
  //  void execute(Set<String> configuration) throws IOException, InterruptedException;
  //
  //  void execute(String mainAdapter, String[] args) throws InterruptedException, IOException;

  String[] configurationAsMainArguments(Set<String> configuration);

  Set<String> configurationAsSet(String[] configuration);
}
