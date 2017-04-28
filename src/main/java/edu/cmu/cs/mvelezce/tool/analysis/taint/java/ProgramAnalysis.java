package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import edu.cmu.cs.mvelezce.tool.pipeline.sleep.SleepRegion;
import org.apache.commons.collections4.map.HashedMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

/**
 * Created by mvelezce on 4/28/17.
 */
public class ProgramAnalysis {

    public static final String DIRECTORY = "output/analysis/java/programs";
    public static final String DOT_JSON = ".json";

    // JSON strings
    public static final String ANALYSIS = "analysis";
    public static final String ID = "ID";
    public static final String OPTIONS = "options";
    public static final String REGION_PACKAGE = "regionPackage";
    public static final String REGION_CLASS = "regionClass";
    public static final String REGION_METHOD = "regionMethod";
    public static final String START_BYTECODE_INDEX = "startBytecodeIndex";
    public static final String END_BYTCODE_INDEX = "endBytecodeIndex"; // TODO this could be array if there are multiple exit points

    public static Map<JavaRegion, Set<String>> analyse(String programName, String mainClass, List<String> programFiles) throws IOException {
        // Reset
        Regions.reset();
        PerformanceEntry.reset();

        // Check if we already have this information
        String outputFile = ProgramAnalysis.DIRECTORY + "/" + programName + ProgramAnalysis.DOT_JSON;
        File file = new File(outputFile);

        if(file.exists()) {
            try {
                return ProgramAnalysis.readFromFile(file);
            } catch (ParseException pe) {
                throw new RuntimeException("Could not parse the cached results");
            }
        }

        // TODO call Lotrack
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 23, 24);
        Regions.addRegion(region1);

        // Regions to options
        Map<JavaRegion, Set<String>> relevantRegionToOptions = new HashedMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        relevantRegionToOptions.put(region1, options);
        // TODO call Lotrack

        ProgramAnalysis.writeToFile(programName, relevantRegionToOptions);

        return relevantRegionToOptions;
    }

    private static void writeToFile(String programName, Map<JavaRegion, Set<String>> relevantRegionsToOptions) throws IOException {
        JSONArray regions = new JSONArray();

        for(Map.Entry<JavaRegion, Set<String>> relevantRegionToOptions : relevantRegionsToOptions.entrySet()) {
            JavaRegion javaRegion = relevantRegionToOptions.getKey();
            JSONObject region = new JSONObject();
            region.put(ProgramAnalysis.ID, javaRegion.getRegionID());
            region.put(ProgramAnalysis.REGION_PACKAGE, javaRegion.getRegionPackage());
            region.put(ProgramAnalysis.REGION_CLASS, javaRegion.getRegionClass());
            region.put(ProgramAnalysis.REGION_METHOD, javaRegion.getRegionMethod());
            region.put(ProgramAnalysis.START_BYTECODE_INDEX, javaRegion.getStartBytecodeIndex());
            region.put(ProgramAnalysis.END_BYTCODE_INDEX, javaRegion.getEndBytecodeIndex());

            JSONArray options = new JSONArray();

            for(String option : relevantRegionToOptions.getValue()) {
                options.add(option);
            }

            region.put(ProgramAnalysis.OPTIONS, options);

            regions.add(region);
        }

        JSONObject result = new JSONObject();
        result.put(ProgramAnalysis.ANALYSIS, regions);

        File directory = new File(ProgramAnalysis.DIRECTORY);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        String outputFile = ProgramAnalysis.DIRECTORY + "/" + programName + ProgramAnalysis.DOT_JSON;
        File file = new File(outputFile);
        FileWriter writer = new FileWriter(file);
        writer.write(result.toJSONString());
        writer.flush();
        writer.close();
    }

    private static Map<JavaRegion, Set<String>> readFromFile(File file) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(new FileReader(file));

        Map<JavaRegion, Set<String>> relevantRegionsToOptions = new HashMap<>();

        JSONArray regions = (JSONArray) result.get(ANALYSIS);

        for(Object entry : regions) {
            JSONObject regionResult = (JSONObject) entry;

            String id = (String) regionResult.get(ProgramAnalysis.ID);
            String regionPackage = (String) regionResult.get(ProgramAnalysis.REGION_PACKAGE);
            String regionClass = (String) regionResult.get(ProgramAnalysis.REGION_CLASS);
            String regionMethod = (String) regionResult.get(ProgramAnalysis.REGION_METHOD);
            int startBytecodeIndex = (int) (long) regionResult.get(ProgramAnalysis.START_BYTECODE_INDEX);
            int endBytecodeIndex = (int) (long) regionResult.get(ProgramAnalysis.END_BYTCODE_INDEX);

            JavaRegion javaRegion = new JavaRegion(id, regionPackage, regionClass, regionMethod, startBytecodeIndex, endBytecodeIndex);

            Set<String> options = new HashSet<>();
            JSONArray optionsResult = (JSONArray) regionResult.get(ProgramAnalysis.OPTIONS);

            for(Object optionResult : optionsResult) {
                options.add((String) optionResult);
            }

            relevantRegionsToOptions.put(javaRegion, options);
        }

        return relevantRegionsToOptions;
    }

}
