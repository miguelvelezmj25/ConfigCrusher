package edu.cmu.cs.mvelezce.tool.compression.serialize;

import java.util.Set;

public class CompressedConfigurations {

    private Set<Set<String>> compressedConfigurations;

    private CompressedConfigurations() {
        ;
    }

    public CompressedConfigurations(Set<Set<String>> compressedConfigurations) {
        this.compressedConfigurations = compressedConfigurations;
    }

    public Set<Set<String>> getCompressedConfigurations() {
        return compressedConfigurations;
    }

    public void setCompressedConfigurations(Set<Set<String>> compressedConfigurations) {
        this.compressedConfigurations = compressedConfigurations;
    }

}
