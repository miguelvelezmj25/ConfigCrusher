package edu.cmu.cs.mvelezce.tool.compression;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface Compression {

  Set<Set<String>> compressConfigurations();

  Set<Set<String>> compressConfigurations(String[] args) throws IOException;

  void writeToFile(Set<Set<String>> compressedConfigurations) throws IOException;

  Set<Set<String>> readFromFile(File file) throws IOException;

  String getOutputDir();

}
