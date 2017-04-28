package edu.cmu.cs.mvelezce.tool.compression;

import edu.cmu.cs.mvelezce.tool.Options;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

/**
 * Created by mvelezce on 4/28/17.
 */
public class Simple extends Options {

    public static final String DIRECTORY = Options.DIRECTORY + "/compression/java/programs";

    // JSON strings
    public static final String COMPRESSION = "compression";
    public static final String CONFIGURATION = "configuration";

    public static Set<Set<String>> getConfigurationsToExecute(String programName, String[] args, Set<Set<String>> relevantOptionsSet) throws IOException {
        Options.getCommandLine(args);

        String outputFile = Simple.DIRECTORY + "/" + programName + Simple.DOT_JSON;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

        if(file.exists()) {
            try {
                return Simple.readFromFile(file);
            }
            catch (ParseException pe) {
                throw new RuntimeException("Could not parse the cached results");
            }
        }

        Set<Set<String>> configurationsToExecute = Simple.getConfigurationsToExecute(relevantOptionsSet);

        if(Options.checkIfSave()) {
            Simple.writeToFile(programName, configurationsToExecute);
        }

        return configurationsToExecute;
    }

    public static Set<Set<String>> getConfigurationsToExecute(Set<Set<String>> relevantOptionsSet) {
        // Calculates which options are subsets of other options
        Set<Set<String>> filteredOptions = Simple.filterOptions(relevantOptionsSet);

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

        while(optionsToConfigurationsToExecuteIterator.hasNext()) {
            Map.Entry<Set<String>, Set<Set<String>>> entry2 = optionsToConfigurationsToExecuteIterator.next();
            Set<String> pivotOptions = new HashSet<>(entry1.getKey());
            pivotOptions.retainAll(entry2.getKey());

            configurationsToExecute = new HashSet<>();

            if(entry1.getValue().size() <= entry2.getValue().size()) {
                Simple.simpleMerging(entry1, entry2, pivotOptions, configurationsToExecute);
            }
            else {
                Simple.simpleMerging(entry2, entry1, pivotOptions, configurationsToExecute);
            }

            Set<String> newCalculatedOptions = new HashSet<>(entry1.getKey());
            newCalculatedOptions.addAll(entry2.getKey());
            Map<Set<String>, Set<Set<String>>> entryHolder = new HashMap<>();
            entryHolder.put(newCalculatedOptions, configurationsToExecute);
            entry1 = entryHolder.entrySet().iterator().next();
        }

        return configurationsToExecute;
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

    private static void simpleMerging(Map.Entry<Set<String>, Set<Set<String>>> largeEntry, Map.Entry<Set<String>, Set<Set<String>>> smallEntry, Set<String> pivotOptions, Set<Set<String>> configurationsToExecute) {
        Iterator<Set<String>> largeSet = largeEntry.getValue().iterator();
        Iterator<Set<String>> smallSet = smallEntry.getValue().iterator();

//        System.out.println("entry 1 size: " + largeEntry.getValue().size());
//        System.out.println("entry 2 size: " + smallEntry.getValue().size());

        while(largeSet.hasNext() && smallSet.hasNext()) {
            Set<String> configurationInLargeSet = largeSet.next();
//            System.out.println("Set1: " + configurationInLargeSet);

            Set<String> valuePivotOptionsInLargeSet = new HashSet<>(configurationInLargeSet);
            valuePivotOptionsInLargeSet.retainAll(pivotOptions);
            boolean merged = false;

            while(smallSet.hasNext()) {
                Set<String> configurationInSmallSet = smallSet.next();
//                System.out.println("Set2: " + configurationInSmallSet);

                Set<String> valuePivotOptionsInSmallSet = new HashSet<>(configurationInSmallSet);
                valuePivotOptionsInSmallSet.retainAll(pivotOptions);

                if(valuePivotOptionsInLargeSet.equals(valuePivotOptionsInSmallSet)) {
//                    System.out.println("Can compress");
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
//                System.out.println("Just add");
//                    throw new RuntimeException("Could not merge the sets");
            }

            smallSet = smallEntry.getValue().iterator();
        }

        while(largeSet.hasNext()) {
            configurationsToExecute.add(largeSet.next());
//            System.out.println("Added rest from largetset");
        }

        while(smallSet.hasNext()) {
            configurationsToExecute.add(smallSet.next());
//            System.out.println("Added rest from smallset");
        }
    }

    private static void writeToFile(String programName, Set<Set<String>> configurationsToExecute) throws IOException {
        JSONArray configurations = new JSONArray();

        for(Set<String> configurationToExecute : configurationsToExecute) {
            JSONArray configuration = new JSONArray();

            for(String value : configurationToExecute) {
                configuration.add(value);
            }

            configurations.add(configuration);
        }

        JSONObject result = new JSONObject();
        result.put(Simple.COMPRESSION, configurations);

        File directory = new File(Simple.DIRECTORY);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        String outputFile = Simple.DIRECTORY + "/" + programName + Simple.DOT_JSON;
        File file = new File(outputFile);
        FileWriter writer = new FileWriter(file);
        writer.write(result.toJSONString());
        writer.flush();
        writer.close();
    }

    private static Set<Set<String>> readFromFile(File file) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(new FileReader(file));

        Set<Set<String>> configurationsToExecute = new HashSet<>();

        JSONArray configurations = (JSONArray) result.get(Simple.COMPRESSION);

        for(Object entry : configurations) {
            JSONArray configurationsResult = (JSONArray) entry;

            Set<String> configuration = new HashSet<>();
            for(Object configurationResult : configurationsResult) {
                configuration.add((String) configurationResult);
            }

            configurationsToExecute.add(configuration);
        }

        return configurationsToExecute;
    }
}
