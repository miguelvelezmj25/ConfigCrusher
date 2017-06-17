package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 4/28/17.
 */
public class ProgramAnalysis {

    public static final String DIRECTORY = Options.DIRECTORY + "/analysis/java/programs";

    // Lotrack strings
    public static final String PACKAGE = "Package";
    public static final String CLASS = "Class";
    public static final String METHOD = "Method";
    public static final String JAVA_LINE_NO = "JavaLineNo";
    public static final String JIMPLE_LINE_NO = "JimpleLineNo";
    public static final String CONSTRAINT = "Constraint";
    public static final String CONSTRAINT_PRETTY = "ConstraintPretty";
    public static final String BYTECODE_INDEXES = "bytecodeIndexes";
    public static final String METHOD_BYTECODE_SIGNATURE_JOANA_STYLE = "methodBytecodeSignatureJoanaStyle";
    public static final String USED_TERMS = "usedTerms";

    public static final String LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL = "_";


    // JSON strings
    public static final String ANALYSIS = "analysis";
    public static final String ID = "ID";
    public static final String OPTIONS = "options";
    public static final String REGION_PACKAGE = "regionPackage";
    public static final String REGION_CLASS = "regionClass";
    public static final String REGION_METHOD = "regionMethod";
    public static final String REGION_JAVA_LINE_NUMBER = "regionJavaLineNumber";
//    public static final String START_BYTECODE_INDEX = "startBytecodeIndex";
//    public static final String END_BYTCODE_INDEX = "endBytecodeIndex"; // TODO this could be array if there are multiple exit points

    public static Map<JavaRegion, Set<String>> analyse(String programName, String[] args, String mainClass, List<String> programFiles, Map<JavaRegion, Set<String>> relevantRegionToOptions) throws IOException {
        Options.getCommandLine(args);

        String outputFile = ProgramAnalysis.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

        if(file.exists()) {
            try {
                return ProgramAnalysis.readFromFile(file);
            }
            catch (ParseException pe) {
                throw new RuntimeException("Could not parse the cached results");
            }
        }

        relevantRegionToOptions = ProgramAnalysis.analyse(programName, mainClass, programFiles, relevantRegionToOptions);

        if(Options.checkIfSave()) {
            ProgramAnalysis.writeToFile(programName, relevantRegionToOptions);
        }

        return relevantRegionToOptions;
    }

    public static Map<JavaRegion, Set<String>> analyse(String programName, String[] args, String database, String program) throws IOException, ParseException {
        Options.getCommandLine(args);

        String outputFile = ProgramAnalysis.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

        if(file.exists()) {
            try {
                return ProgramAnalysis.readFromFile(file);
            }
            catch (ParseException pe) {
                throw new RuntimeException("Could not parse the cached results");
            }
        }

        Map<JavaRegion, Set<String>> relevantRegionToOptions = ProgramAnalysis.analyse(database, program);

        if(Options.checkIfSave()) {
            ProgramAnalysis.writeToFile(programName, relevantRegionToOptions);
        }

        return relevantRegionToOptions;
    }

    // TODO the parameters are for testing. We should not be passing the map since that is what this component needs to calculate
    public static Map<JavaRegion, Set<String>> analyse(String programName, String mainClass, List<String> programFiles, Map<JavaRegion, Set<String>> relevantRegionToOptions) {
        return relevantRegionToOptions;
    }

    public static Map<JavaRegion, Set<String>> analyse(String database, String program) throws ParseException {
        // This is hardcode to get the output of Lotrack
        List<String> fields = new ArrayList<>();
        fields.add(ProgramAnalysis.PACKAGE);
        fields.add(ProgramAnalysis.CLASS);
        fields.add(ProgramAnalysis.METHOD);
        fields.add(ProgramAnalysis.JAVA_LINE_NO);
        fields.add(ProgramAnalysis.JIMPLE_LINE_NO);
        fields.add(ProgramAnalysis.CONSTRAINT);
        fields.add(ProgramAnalysis.CONSTRAINT_PRETTY);
        fields.add(ProgramAnalysis.BYTECODE_INDEXES);
        fields.add(ProgramAnalysis.METHOD_BYTECODE_SIGNATURE_JOANA_STYLE);
        fields.add(ProgramAnalysis.USED_TERMS);

        List<String> sortBy = new ArrayList<>();
        sortBy.add(ProgramAnalysis.PACKAGE);
        sortBy.add(ProgramAnalysis.CLASS);
        sortBy.add(ProgramAnalysis.METHOD);
        sortBy.add(ProgramAnalysis.BYTECODE_INDEXES);

        ScalaMongoDriverConnector.connect(database);
        List<String> queryResult = ScalaMongoDriverConnector.findProjectionAscending(program, fields, sortBy);
        ScalaMongoDriverConnector.close();

        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
        String currentPackage = "";
        String currentClass = "";
        String currentMethod = "";
        JavaRegion currentJavaRegion = null;
        Set<String> currentUsedTerms = new HashSet<>();
        JSONParser parser = new JSONParser();

        for(String result : queryResult) {
            JSONObject entry = (JSONObject) parser.parse(result);
            List<String> usedTerms = (List<String>) entry.get(ProgramAnalysis.USED_TERMS);

            if(usedTerms.size() == 1 && (usedTerms.contains("true") || usedTerms.contains("false"))) {
                if(currentJavaRegion != null) {
                    regionsToOptions.put(currentJavaRegion, currentUsedTerms);
                    System.out.println(currentJavaRegion.getRegionClass() + " " + currentJavaRegion.getRegionMethod() + " " + currentJavaRegion.getJavaLineNubmer() + " with " + currentUsedTerms);
                }

                currentJavaRegion = null;
                currentUsedTerms = new HashSet<>();
                continue;
            }

            String entryPackage = (String) entry.get(ProgramAnalysis.PACKAGE);
            String entryClass = (String) entry.get(ProgramAnalysis.CLASS);

            if(entryClass.contains(".")) {
                entryClass = entryClass.substring(entryClass.lastIndexOf(".") + 1);
            }

            String entryMethod = (String) entry.get(ProgramAnalysis.METHOD);

            if(!currentPackage.equals(entryPackage) ||!currentClass.equals(entryClass) || !currentMethod.equals(entryMethod)) {
                currentUsedTerms = new HashSet<>();
                currentPackage = entryPackage;
                currentClass = entryClass;
                currentMethod = entryMethod;
            }

            if(!currentUsedTerms.containsAll(usedTerms)) {
                currentUsedTerms.addAll(usedTerms);

                if(currentJavaRegion == null) {
                    currentJavaRegion = new JavaRegion(entryPackage, entryClass, entryMethod);
                    int entryJavaLineNumber = (int)(long) entry.get(ProgramAnalysis.JAVA_LINE_NO);
                    currentJavaRegion.setJavaLineNubmer(entryJavaLineNumber);
                }
            }
        }

        return regionsToOptions;
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
            region.put(ProgramAnalysis.REGION_METHOD, javaRegion.getRegionMethod());
            region.put(ProgramAnalysis.REGION_JAVA_LINE_NUMBER, javaRegion.getJavaLineNubmer());
//            region.put(ProgramAnalysis.START_BYTECODE_INDEX, javaRegion.getStartBytecodeIndex());
//            region.put(ProgramAnalysis.END_BYTCODE_INDEX, javaRegion.getEndBytecodeIndex());

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

        String outputFile = ProgramAnalysis.DIRECTORY + "/" + programName + Options.DOT_JSON;
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
            int regionJavaLineNumber = (int) (long) regionResult.get(ProgramAnalysis.REGION_JAVA_LINE_NUMBER);
//            int startBytecodeIndex = (int) (long) regionResult.get(ProgramAnalysis.START_BYTECODE_INDEX);
//            int endBytecodeIndex = (int) (long) regionResult.get(ProgramAnalysis.END_BYTCODE_INDEX);

            JavaRegion javaRegion = new JavaRegion(id, regionPackage, regionClass, regionMethod);
            javaRegion.setJavaLineNubmer(regionJavaLineNumber);

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
