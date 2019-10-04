package edu.cmu.cs.mvelezce.analysis;

import java.io.File;
import java.io.IOException;

public interface Analysis<T> {

  T analyze() throws IOException, InterruptedException;

  T analyze(String[] args) throws IOException, InterruptedException;

  void writeToFile(T value) throws IOException;

  T readFromFile(File file) throws IOException;

  String outputDir();
}
