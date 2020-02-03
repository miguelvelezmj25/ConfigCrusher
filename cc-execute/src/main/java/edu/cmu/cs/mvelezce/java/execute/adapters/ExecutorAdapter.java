package edu.cmu.cs.mvelezce.java.execute.adapters;

import java.io.IOException;
import java.util.Set;

public interface ExecutorAdapter {

  String USER_HOME = System.getProperty("user.home");
  String PATH_SEPARATOR = System.getProperty("path.separator");
  String EXECUTOR_MAIN_CLASS_PREFIX = "edu.cmu.cs.mvelezce.cc.executor.CC";

  void execute(Set<String> configuration) throws IOException, InterruptedException;
}
