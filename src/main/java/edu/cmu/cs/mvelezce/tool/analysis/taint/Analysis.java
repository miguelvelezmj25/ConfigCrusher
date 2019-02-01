package edu.cmu.cs.mvelezce.tool.analysis.taint;

import java.io.File;
import java.io.IOException;

public interface Analysis<T> {

  T analyze() throws Exception;

  T analyze(String[] args) throws Exception;

  void writeToFile(T relevantRegionsToOptions) throws IOException;

  T readFromFile(File file) throws IOException;

  String outputDir();

}
