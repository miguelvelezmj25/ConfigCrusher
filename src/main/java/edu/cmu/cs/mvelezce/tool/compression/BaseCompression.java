package edu.cmu.cs.mvelezce.tool.compression;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseCompression implements Compression {

    private String programName = null;

    public BaseCompression() { }

    public BaseCompression(String programName) {
        this.programName = programName;
    }

    public String getProgramName() {
        return this.programName;
    }

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

    public static Set<Set<String>> expandOptions(Collection<Set<Set<String>>> optionsSets) {
        Set<Set<String>> result = new HashSet<>();

        for(Set<Set<String>> optionsSet : optionsSets) {
            result.addAll(optionsSet);
        }

        return result;
    }

}
