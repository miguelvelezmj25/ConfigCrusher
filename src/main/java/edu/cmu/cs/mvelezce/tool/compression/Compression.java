package edu.cmu.cs.mvelezce.tool.compression;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface Compression {

    public Set<Set<String>> compressConfigurations(String[] args) throws IOException;

    public Set<Set<String>> compressConfigurations(String[] args, Set<Set<String>> relevantOptionsSet) throws IOException;

    public Set<Set<String>> compressConfigurations(Set<Set<String>> relevantOptionsSet);

    public void writeToFile(Set<Set<String>> compressedConfigurations) throws IOException;

    public Set<Set<String>> readFromFile(File file) throws IOException;

}
