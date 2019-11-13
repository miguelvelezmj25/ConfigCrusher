package edu.cmu.cs.mvelezce.java.execute.adapters;

import java.io.IOException;
import java.util.Set;

public interface ExecutorAdapter {

  String EXECUTOR_MAIN_CLASS_PREFIX = "edu.cmu.cs.mvelezce.cc.executor.CC";

  void execute(Set<String> configuration) throws IOException, InterruptedException;
}
