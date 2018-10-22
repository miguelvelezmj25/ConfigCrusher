package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Adapter {

  void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException;

  void execute(Set<String> configuration) throws IOException, InterruptedException;

  void execute(String mainAdapter, String[] args) throws InterruptedException, IOException;

  String[] configurationAsMainArguments(Set<String> configuration);

  Set<String> configurationAsSet(String[] configuration);

  String getProgramName();

  String getMainClass();

  String getDirectory();

  List<String> getOptions();

}
