package edu.cmu.cs.mvelezce.tool.compression;

import java.io.File;
import java.io.IOException;

public interface Compression<T> {

  T compressConfigurations();

  T compressConfigurations(String[] args) throws IOException;

  void writeToFile(T compressedConfigurations) throws IOException;

  T readFromFile(File file) throws IOException;

  String getOutputDir();

}
