package edu.cmu.cs.mvelezce.java.execute;

import java.io.IOException;

public interface Executor {

  void execute(String[] args) throws IOException, InterruptedException;

  void execute(int iterations) throws InterruptedException, IOException;

  void executeIteration(int iteration) throws InterruptedException, IOException;

  String outputDir();
}
