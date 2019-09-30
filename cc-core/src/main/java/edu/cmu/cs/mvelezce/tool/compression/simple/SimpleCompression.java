package edu.cmu.cs.mvelezce.tool.compression.simple;

import edu.cmu.cs.mvelezce.tool.compression.BaseCompression;

import java.util.*;

/**
 * Created by mvelezce on 4/28/17.
 */
public class SimpleCompression extends BaseCompression {

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
    public Set<Set<String>> compressConfigurations() {
        // Calculates which options are subsets of other options
        Set<Set<String>> filteredOptions = BaseCompression.filterOptions(this.getOptionSet());

        // Get the configurations for each option
        Map<Set<String>, Set<Set<String>>> optionsToConfigurationsToExecute = new HashMap<>();

        for(Set<String> options : filteredOptions) {
            Set<Set<String>> configurationsToExecuteForOption = new HashSet<>(edu.cmu.cs.mvelezce.tool.Helper.getConfigurations(options));
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

        while(optionsToConfigurationsToExecuteIterator.hasNext()) {
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

    @Override
    public String getOutputDir() {
        return BaseCompression.DIRECTORY;
    }

    private void simpleMerging(Map.Entry<Set<String>, Set<Set<String>>> largeEntry, Map.Entry<Set<String>, Set<Set<String>>> smallEntry, Set<String> pivotOptions, Set<Set<String>> configurationsToExecute) {
        Map<Set<String>, List<Set<String>>> largePivotToConfigs = this.pivotValueToConfigs(pivotOptions, largeEntry.getValue());
        Map<Set<String>, List<Set<String>>> smallPivotToConfigs = this.pivotValueToConfigs(pivotOptions, smallEntry.getValue());
        Set<Set<String>> pivotValues = new HashSet<>();
        pivotValues.addAll(largePivotToConfigs.keySet());
        pivotValues.addAll(smallPivotToConfigs.keySet());

        for(Set<String> pivotValue : pivotValues) {
            List<Set<String>> largeConfigs = largePivotToConfigs.get(pivotValue);
            List<Set<String>> smallConfigs = smallPivotToConfigs.get(pivotValue);

            if(largeConfigs != null && smallConfigs != null) {
                Iterator<Set<String>> largeSet = largeConfigs.iterator();
                Iterator<Set<String>> smallSet = smallConfigs.iterator();

                while(largeSet.hasNext() && smallSet.hasNext()) {
                    Set<String> largeConfig = largeSet.next();
                    Set<String> smallConfig = smallSet.next();

                    Set<String> compressedConfiguration = new HashSet<>(largeConfig);
                    compressedConfiguration.addAll(smallConfig);
                    configurationsToExecute.add(compressedConfiguration);
                }

                while(largeSet.hasNext()) {
                    configurationsToExecute.add(largeSet.next());
                }

                while(smallSet.hasNext()) {
                    configurationsToExecute.add(smallSet.next());
                }
            }
            else if(largeConfigs != null) {
                configurationsToExecute.addAll(largeConfigs);
            }
            else {
                configurationsToExecute.addAll(smallConfigs);
            }

        }

    }

    private Map<Set<String>, List<Set<String>>> pivotValueToConfigs(Set<String> pivot, Set<Set<String>> configs) {
        Map<Set<String>, List<Set<String>>> pivotValueToConfigs = new HashMap<>();

        for(Set<String> conf : configs) {
            Set<String> pivotValue = new HashSet<>(conf);
            pivotValue.retainAll(pivot);

            List<Set<String>> confs = new ArrayList<>();

            if(pivotValueToConfigs.containsKey(pivotValue)) {
                confs = pivotValueToConfigs.get(pivotValue);
            }

            confs.add(conf);
            pivotValueToConfigs.put(pivotValue, confs);
        }

        return pivotValueToConfigs;
    }

}
