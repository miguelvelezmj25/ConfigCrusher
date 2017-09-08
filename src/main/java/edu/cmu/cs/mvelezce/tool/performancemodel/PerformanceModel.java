package edu.cmu.cs.mvelezce.tool.performancemodel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// TODO time can be in seconds, milliseonds, minutes,.... That affects the type of the block.

/**
 * Created by mvelezce on 3/9/17.
 */
public class PerformanceModel {

    private long baseTime = 0;
    private double baseTimeHumanReadable = 0.0;
    private Map<Region, Map<Set<String>, Long>> regionsToPerformanceTables = new HashMap<>();
    private Map<Region, Map<Set<String>, Double>> regionsToPerformanceTablesHumanReadable = new HashMap<>();


//    private Map<Set<String>, Double> configurationToPerformance = new HashMap<>();
//    private List<Map<Set<String>, Double>> tablesOfRegions = new ArrayList<>();
////    private MultiValuedMap<Set<String>, Map<Set<String>, Double>> regionToInfluenceTable;

    private PerformanceModel() {
        ;
    }

    public PerformanceModel(long baseTime, Map<Region, Map<Set<String>, Long>> regionsToPerformanceTables) {
        this.baseTime = baseTime;
        this.regionsToPerformanceTables = regionsToPerformanceTables;

        this.baseTimeHumanReadable = PerformanceEntry2.toHumanReadable(PerformanceEntry2.toSeconds(this.baseTime));
        this.regionsToPerformanceTablesHumanReadable = PerformanceModel.toHumanReadable(this.regionsToPerformanceTables);
    }

    /**
     * The assumption is that the values in the tables are in nanoseconds.
     *
     * @param regionsToPerformanceTables
     * @return
     */
    private static Map<Region, Map<Set<String>, Double>> toHumanReadable(Map<Region, Map<Set<String>, Long>> regionsToPerformanceTables) {
        Map<Region, Map<Set<String>, Double>> result = new HashMap<>();

        for(Map.Entry<Region, Map<Set<String>, Long>> regionToTable : regionsToPerformanceTables.entrySet()) {
            Map<Set<String>, Double> configToNewPerformance = new HashMap<>();

            for(Map.Entry<Set<String>, Long> configToPerformance : regionToTable.getValue().entrySet()) {
                double seconds = PerformanceEntry2.toSeconds(configToPerformance.getValue());
                configToNewPerformance.put(configToPerformance.getKey(), PerformanceEntry2.toHumanReadable(seconds));
            }

            result.put(regionToTable.getKey(), configToNewPerformance);
        }

        return result;
    }

//    public PerformanceModel(double baseTime, List<Map<Set<String>, Double>> blocks) {
//        this.baseTime = baseTime;
//        this.configurationToPerformance = new HashMap<>();
//
////        // TODO this is a hacky way of creating an empty performancemodel model. Either add new constructor or move the methods below to a method call
////        if(blocks.isEmpty()) {
////            return ;
////        }
//
//        this.calculateConfigurationInfluence(blocks);
//    }

//    public static Map<Set<String>, Double> calculateConfigurationsInfluence(Map<Set<String>, Double> regionTable) {
//        Set<String> actualRegionOptions = new HashSet<>();
//
//        for(Map.Entry<Set<String>, Double> entry : regionTable.entrySet()) {
//            actualRegionOptions.addAll(entry.getKey());
//        }
//
//        if(Math.pow(2, actualRegionOptions.size()) != regionTable.size()) {
////            return null;
//            throw new RuntimeException("FAILED");
//        }
//
//        int numberOfOptions = (int) (Math.log(regionTable.size()) / Math.log(2));
//        Set<String> regionOptions = new HashSet<>();
//
//        for(Map.Entry<Set<String>, Double> entry : regionTable.entrySet()) {
//            if(entry.getKey().size() == numberOfOptions) {
//                regionOptions.addAll(entry.getKey());
//            }
//        }
//
//        Map<Set<String>, Double> configurationToInfluence = new HashMap<>();
//
//        if(numberOfOptions == regionOptions.size()) {
//            PerformanceModel.calculateConfigurationInfluence(regionOptions, regionTable, configurationToInfluence);
//        }
//        else {
////            System.out.println(regionTable.keySet());
////
////            double t = 0;
////
////            for(Double time : regionTable.values()) {
////                t += time;
////            }
////
////            t = t / regionTable.size();
////
////            configurationToInfluence.put(new HashSet<>(), t);
//            throw new RuntimeException("f");
////            int smallestSize = Integer.MAX_VALUE;
////
////            for(Map.Entry<Set<String>, Double> table : regionTable.entrySet()) {
////                if(table.getKey().size() < smallestSize) {
////                    smallestSize = table.getKey().size();
////                }
////            }
////
////            Set<String> smallest = new HashSet<>();
////
////            for(Map.Entry<Set<String>, Double> table : regionTable.entrySet()) {
////                if(table.getKey().size() == smallestSize) {
////                    smallest = new HashSet<>(table.getKey());
////                }
////            }
////
////            configurationToInfluence.put(smallest, regionTable.get(smallest));
//
////            for(Map.Entry<Set<String>, Double> entry : regionTable.entrySet()) {
////                configurationToInfluence.put(entry.getKey(), entry.getValue());
////            }
//        }
//
//        return configurationToInfluence;
//    }
//
//    public static double calculateConfigurationInfluence(Set<String> longestConfiguration, Map<Set<String>, Double> configurationsToPerformance, Map<Set<String>, Double> configurationToInfluence) {
//        if(!configurationToInfluence.containsKey(longestConfiguration)) {
//            int currentLength = longestConfiguration.size();
//            double influence = configurationsToPerformance.get(longestConfiguration);
//
//            if(currentLength > 0) {
//                for(Map.Entry<Set<String>, Double> entry : configurationsToPerformance.entrySet()) {
//                    Set<String> configuration = entry.getKey();
//                    Set<String> intersectionWithLongestConfiguration = new HashSet<>(longestConfiguration);
//                    intersectionWithLongestConfiguration.retainAll(configuration);
//
//                    if(configuration.size() < currentLength && intersectionWithLongestConfiguration.equals(configuration)/*!intersectionWithLongestConfiguration.isEmpty()*/) {
//                        influence -= PerformanceModel.calculateConfigurationInfluence(entry.getKey(), configurationsToPerformance, configurationToInfluence);
//                    }
//                }
//            }
//
//            configurationToInfluence.put(longestConfiguration, influence);
//        }
//
//        return configurationToInfluence.get(longestConfiguration);
//    }
//
//    public void calculateConfigurationInfluence(List<Map<Set<String>, Double>> bfTablePerRegion) {
//        MultiValuedMap<Set<String>, Map<Set<String>, Double>> regionToInfluenceTable = new HashSetValuedHashMap<>();
//
//        // Get influence for each table with the same configurations
//        for(Map<Set<String>, Double> bfTable : bfTablePerRegion) {
//            Map<Set<String>, Double> influenceTable = PerformanceModel.calculateConfigurationsInfluence(bfTable);
//
//            Set<String> relevantOptions = new HashSet<>();
//
//            for(Set<String> configuration : bfTable.keySet()) {
//                relevantOptions.addAll(configuration);
//            }
//
//            if(influenceTable == null) {
//                this.tablesOfRegions.add(bfTable);
//            }
//            else {
//                regionToInfluenceTable.put(relevantOptions, influenceTable);
//            }
//        }
//
//        for(Map.Entry<Set<String>, Map<Set<String>, Double>> optionToPerformanceTable : regionToInfluenceTable.entries()) {
//            for(Map.Entry<Set<String>, Double> configurationToPerformance : optionToPerformanceTable.getValue().entrySet()) {
//                double time = configurationToPerformance.getValue();
//
//                if(this.configurationToPerformance.containsKey(configurationToPerformance.getKey())) {
//                    time += this.configurationToPerformance.get(configurationToPerformance.getKey());
//                }
//
//                this.configurationToPerformance.put(configurationToPerformance.getKey(), time);
//            }
//        }
//
////        HashSet<String> emptyConfiguration = new HashSet<>();
////        double emptyConfigurationPerformance = this.baseTime;
////        emptyConfigurationPerformance += this.configurationToPerformance.get(emptyConfiguration);
////        this.configurationToPerformance.put(emptyConfiguration, emptyConfigurationPerformance);
//    }

//    public double evaluate(Set<String> configuration) {
//        double performance = 0;
//
//        for(Map.Entry<Set<String>, Double> entry : this.configurationToPerformance.entrySet()) {
//            Set<String> configurationValueOfOptionInBlock = new HashSet<>(entry.getKey());
//            configurationValueOfOptionInBlock.retainAll(configuration);
//
//            if(entry.getKey().equals(configurationValueOfOptionInBlock)) {
//                performance += entry.getValue();
//            }
//        }
//
//        return performance;
//    }
//
//////    public double getBaseTime() {
//////        return this.baseTime;
//////    }
////
////    public Map<Set<String>, Double> getConfigurationToPerformance() {
////        return this.configurationToPerformance;
////    }
////
////    public void setConfigurationToPerformance(Map<Set<String>, Double> configurationToPerformance) {
////        this.configurationToPerformance = configurationToPerformance;
////    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");

        StringBuilder pm = new StringBuilder();

        pm.append(decimalFormat.format(this.baseTimeHumanReadable));
        pm.append("\n");
        pm.append("\n");

        for(Map<Set<String>, Double> performanceTables : this.regionsToPerformanceTablesHumanReadable.values()) {
            for(Map.Entry<Set<String>, Double> configurationToPerformance : performanceTables.entrySet()) {
                pm.append(configurationToPerformance.getKey());
                pm.append(" -> ");
                pm.append(decimalFormat.format(configurationToPerformance.getValue()));
                pm.append("\n");
            }

            pm.append("\n");
        }

        return pm.toString();
    }
//    @Override
//    public String toString() {
//        DecimalFormat decimalFormat = new DecimalFormat("#.##");
//        StringBuilder performanceModel = new StringBuilder("T =");
//        Set<String> allOptions = new HashSet<>();
//
//        for(Set<String> configurations : this.configurationToPerformance.keySet()) {
//            allOptions.addAll(configurations);
//        }
//
//        for(int i = 0; i <= allOptions.size(); i++) {
//            for(Map.Entry<Set<String>, Double> entry : this.configurationToPerformance.entrySet()) {
//                if(entry.getKey().size() != i) {
//                    continue;
//                }
//
//                if(Math.abs(entry.getValue()) < 0.01) {
//                    continue;
//                }
//
//                if(entry.getValue() > 0) {
//                    performanceModel.append(" + ");
//                }
//                else {
//                    performanceModel.append(" - ");
//                }
//
//                performanceModel.append(decimalFormat.format(Math.abs(entry.getValue())));
//
//                for(String option : entry.getKey()) {
//                    performanceModel.append(option).append(" ");
//                }
//
//                if(!entry.getKey().isEmpty()) {
//                    performanceModel.deleteCharAt(performanceModel.length() - 1);
//                }
//            }
//
//        }
//
//        return performanceModel.toString();
//    }

//    @Override
//    public boolean equals(Object o) {
//        if(this == o) {
//            return true;
//        }
//        if(o == null || getClass() != o.getClass()) {
//            return false;
//        }
//
//        PerformanceModel that = (PerformanceModel) o;
//
//        return configurationToPerformance.equals(that.configurationToPerformance);
//    }
//
//    @Override
//    public int hashCode() {
//        return configurationToPerformance.hashCode();
//    }


    public long getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
    }

    public double getBaseTimeHumanReadable() {
        return baseTimeHumanReadable;
    }

    public void setBaseTimeHumanReadable(double baseTimeHumanReadable) {
        this.baseTimeHumanReadable = baseTimeHumanReadable;
    }

    public Map<Region, Map<Set<String>, Long>> getRegionsToPerformanceTables() {
        return regionsToPerformanceTables;
    }

    public void setRegionsToPerformanceTables(Map<Region, Map<Set<String>, Long>> regionsToPerformanceTables) {
        this.regionsToPerformanceTables = regionsToPerformanceTables;
    }

    public Map<Region, Map<Set<String>, Double>> getRegionsToPerformanceTablesHumanReadable() {
        return regionsToPerformanceTablesHumanReadable;
    }

    public void setRegionsToPerformanceTablesHumanReadable(Map<Region, Map<Set<String>, Double>> regionsToPerformanceTablesHumanReadable) {
        this.regionsToPerformanceTablesHumanReadable = regionsToPerformanceTablesHumanReadable;
    }

//    public static class MyMapDeserializer extends KeyDeserializer {
//
//        @Override
//        public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
//            return null;
//        }
//    }
//
//    public static class YourClassKeyDeserializer extends KeyDeserializer {
//
//        @Override
//        public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
//            return null;
//        }
//    }
//
//    public static class PerformanceModelDeserializer extends JsonDeserializer<PerformanceModel> {
//
////        @Override
////        public DecisionAndOptions deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
////            ObjectCodec oc = jsonParser.getCodec();
////            JsonNode node = oc.readTree(jsonParser);
////
////            ObjectMapper mapper = new ObjectMapper();
////            ObjectReader reader = mapper.readerFor(new TypeReference<Set<Set<String>>>() {
////            });
////            Set<Set<String>> optionsSet = reader.readValue(node.get("options"));
////
////            JsonNode region = node.get("region");
////
////            JavaRegion javaRegion = new JavaRegion(region.get("regionID").asText(), region.get("regionPackage").asText(),
////                    region.get("regionClass").asText(), region.get("regionMethod").asText(),
////                    region.get("startBytecodeIndex").intValue());
////
////            return new DecisionAndOptions(javaRegion, optionsSet);
////        }
//
//        @Override
//        public PerformanceModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
//            ObjectMapper mapper = new ObjectMapper();
//
//            SimpleModule simpleModule = new SimpleModule();
//            simpleModule.addKeyDeserializer(PerformanceModel.class, new YourClassKeyDeserializer());
//            mapper.registerModule(simpleModule);
//
//
//            ObjectCodec oc = jsonParser.getCodec();
//            JsonNode node = oc.readTree(jsonParser);
//
//            long baseTime = node.get("baseTime").asLong();
//            double baseTimeHumanReadable = node.get("baseTimeHumanReadable").asDouble();
//
//
//            ObjectReader reader = mapper.readerFor(new TypeReference<Map<Region, Map<Set<String>, Long>>>() {
//            });
//            reader.readValue(node.get("regionsToPerformanceTables"));
//
//            return null;
//        }

}
