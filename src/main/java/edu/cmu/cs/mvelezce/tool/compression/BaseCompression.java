package edu.cmu.cs.mvelezce.tool.compression;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.compression.serialize.CompressedConfigurations;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseCompression implements Compression {

    // TODO pass the set to compress as an instance variable
    private String programName = null;
    private Set<Set<String>> optionSet;

//    public BaseCompression() { }

    public BaseCompression(String programName, Set<Set<String>> optionSet) {
        this.programName = programName;
        this.optionSet = optionSet;
    }

    // TODO should this be static?
    public static Set<Set<String>> filterOptions(Set<Set<String>> relevantOptionsSet) {
        Set<Set<String>> filteredOptions = new HashSet<>();

        for(Set<String> relevantOptions : relevantOptionsSet) {
            if(filteredOptions.isEmpty()) {
                filteredOptions.add(relevantOptions);
                continue;
            }

            Set<Set<String>> optionsToRemove = new HashSet<>();
            Set<Set<String>> optionsToAdd = new HashSet<>();

            for(Set<String> options : filteredOptions) {
                if(options.equals(relevantOptions) || options.containsAll(relevantOptions)) {
                    optionsToAdd.remove(relevantOptions);
                    break;
                }

                if(!options.containsAll(relevantOptions) && relevantOptions.containsAll(options)) {
                    optionsToRemove.add(options);
                }

                optionsToAdd.add(relevantOptions);
            }

            filteredOptions.removeAll(optionsToRemove);
            filteredOptions.addAll(optionsToAdd);
        }

        return filteredOptions;
    }

    // TODO what is this doing?
    public static Set<Set<String>> expandOptions(Collection<Set<Set<String>>> optionsSets) {
        Set<Set<String>> result = new HashSet<>();

        for(Set<Set<String>> optionsSet : optionsSets) {
            result.addAll(optionsSet);
        }

        return result;
    }

    public String getProgramName() {
        return this.programName;
    }

    public Set<Set<String>> getOptionSet() {
        return this.optionSet;
    }

    public void writeToFile(Set<Set<String>> configurationsToExecute) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = SimpleCompression.DIRECTORY + "/" + this.programName + "/" + this.programName
                + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

        CompressedConfigurations compressedConfigurations = new CompressedConfigurations(configurationsToExecute);
        mapper.writeValue(file, compressedConfigurations);
    }

    @Override
    public Set<Set<String>> readFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CompressedConfigurations results = mapper.readValue(file, new TypeReference<CompressedConfigurations>() {
        });

        return results.getCompressedConfigurations();
    }

}
