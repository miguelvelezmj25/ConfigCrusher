package edu.cmu.cs.mvelezce.tool.compression;

import edu.cmu.cs.mvelezce.tool.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 4/28/17.
 */
public class SimpleCompression extends BaseCompression {

    public static final String DIRECTORY = Options.DIRECTORY + "/compression/java/programs";

    public SimpleCompression(Set<Set<String>> optionSet) {
        this(null, optionSet);
    }

    public SimpleCompression(String programName) {
        this(programName, null);
    }

    public SimpleCompression(String programName, Set<Set<String>> optionSet) {
        super(programName, optionSet);
    }

    @Override
    public Set<Set<String>> compressConfigurations(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputFile = SimpleCompression.DIRECTORY + "/" + this.getProgramName();
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

        if(file.exists()) {
            Collection<File> files = FileUtils.listFiles(file, null, true);

            if(files.size() != 1) {
                throw new RuntimeException("We expected to find 1 file in the directory, but that is not the case "
                        + outputFile);
            }

            return this.readFromFile(files.iterator().next());
        }

        Set<Set<String>> configurationsToExecute = this.compressConfigurations();

        if(Options.checkIfSave()) {
            this.writeToFile(configurationsToExecute);
        }

        return configurationsToExecute;
    }

    @Override
    public Set<Set<String>> compressConfigurations() {
        // Calculates which options are subsets of other options
        Set<Set<String>> filteredOptions = BaseCompression.filterOptions(this.getOptionSet());

        // Get the configurations for each option
        Map<Set<String>, Set<Set<String>>> optionsToConfigurationsToExecute = new HashMap<>();

        for(Set<String> options : filteredOptions) {
            Set<Set<String>> configurationsToExecuteForOption = new HashSet<>();
            configurationsToExecuteForOption.addAll(edu.cmu.cs.mvelezce.tool.Helper.getConfigurations(options));
            optionsToConfigurationsToExecute.put(options, configurationsToExecuteForOption);
        }

        // Compresses which configurations to execute
        Set<Set<String>> configurationsToExecute = new HashSet<>();

        if(optionsToConfigurationsToExecute.size() == 0) {
            configurationsToExecute.add(new HashSet<>());
            return configurationsToExecute;
        }

        // Base case covering 1 configuration
        if(optionsToConfigurationsToExecute.size() == 1) {
            configurationsToExecute.addAll(optionsToConfigurationsToExecute.entrySet().iterator().next().getValue());
            return configurationsToExecute;
        }

        Iterator<Map.Entry<Set<String>, Set<Set<String>>>> optionsToConfigurationsToExecuteIterator = optionsToConfigurationsToExecute.entrySet().iterator();
        Map.Entry<Set<String>, Set<Set<String>>> entry1 = optionsToConfigurationsToExecuteIterator.next();

        while (optionsToConfigurationsToExecuteIterator.hasNext()) {
            Map.Entry<Set<String>, Set<Set<String>>> entry2 = optionsToConfigurationsToExecuteIterator.next();
            Set<String> pivotOptions = new HashSet<>(entry1.getKey());
            pivotOptions.retainAll(entry2.getKey());

            configurationsToExecute = new HashSet<>();

            if(entry1.getValue().size() <= entry2.getValue().size()) {
                this.simpleMerging(entry1, entry2, pivotOptions, configurationsToExecute);
            }
            else {
                this.simpleMerging(entry2, entry1, pivotOptions, configurationsToExecute);
            }

            Set<String> newCalculatedOptions = new HashSet<>(entry1.getKey());
            newCalculatedOptions.addAll(entry2.getKey());
            Map<Set<String>, Set<Set<String>>> entryHolder = new HashMap<>();
            entryHolder.put(newCalculatedOptions, configurationsToExecute);
            entry1 = entryHolder.entrySet().iterator().next();
        }

        return configurationsToExecute;
    }

    private void simpleMerging(Map.Entry<Set<String>, Set<Set<String>>> largeEntry, Map.Entry<Set<String>, Set<Set<String>>> smallEntry, Set<String> pivotOptions, Set<Set<String>> configurationsToExecute) {
        Iterator<Set<String>> largeSet = largeEntry.getValue().iterator();
        Iterator<Set<String>> smallSet = smallEntry.getValue().iterator();

        while (largeSet.hasNext() && smallSet.hasNext()) {
            Set<String> configurationInLargeSet = largeSet.next();

            Set<String> valuePivotOptionsInLargeSet = new HashSet<>(configurationInLargeSet);
            valuePivotOptionsInLargeSet.retainAll(pivotOptions);
            boolean merged = false;

            while (smallSet.hasNext()) {
                Set<String> configurationInSmallSet = smallSet.next();

                Set<String> valuePivotOptionsInSmallSet = new HashSet<>(configurationInSmallSet);
                valuePivotOptionsInSmallSet.retainAll(pivotOptions);

                if(valuePivotOptionsInLargeSet.equals(valuePivotOptionsInSmallSet)) {
                    Set<String> compressedConfiguration = new HashSet<>(configurationInLargeSet);
                    compressedConfiguration.addAll(configurationInSmallSet);
                    configurationsToExecute.add(compressedConfiguration);

                    smallEntry.getValue().remove(configurationInSmallSet);
                    merged = true;
                    break;
                }
            }

            if(!merged) {
                Set<String> compressedConfiguration = new HashSet<>(configurationInLargeSet);
                configurationsToExecute.add(compressedConfiguration);
            }

            smallSet = smallEntry.getValue().iterator();
        }

        while (largeSet.hasNext()) {
            configurationsToExecute.add(largeSet.next());
        }

        while (smallSet.hasNext()) {
            configurationsToExecute.add(smallSet.next());
        }
    }

}
