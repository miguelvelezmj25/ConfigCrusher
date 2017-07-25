package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.DecisionAndOptions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
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
    //    public static final String JAVA_LINE_NO = "JavaLineNo";
    public static final String JIMPLE_LINE_NO = "JimpleLineNo";
    public static final String CONSTRAINT = "Constraint";
    public static final String CONSTRAINT_PRETTY = "ConstraintPretty";
    public static final String BYTECODE_INDEXES = "bytecodeIndexes";
    public static final String METHOD_BYTECODE_SIGNATURE_JOANA_STYLE = "methodBytecodeSignatureJoanaStyle";
    public static final String USED_TERMS = "usedTerms";

    // JSON strings
    public static final String ANALYSIS = "analysis";
    public static final String ID = "ID";
    public static final String OPTIONS = "options";
    public static final String REGION_PACKAGE = "regionPackage";
    public static final String REGION_CLASS = "regionClass";
    public static final String REGION_METHOD = "regionMethod";
    //    public static final String REGION_JAVA_LINE_NUMBER = "regionJavaLineNumber";
    public static final String START_BYTECODE_INDEX = "startBytecodeIndex";

    public static Map<JavaRegion, Set<Set<String>>> analyze(String programName, String[] args) throws IOException, ParseException {
        Options.getCommandLine(args);

        String outputFile = ProgramAnalysis.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);
        Map<JavaRegion, Set<Set<String>>> relevantRegionToOptions = null;

        if(file.exists()) {
            try {
                relevantRegionToOptions = ProgramAnalysis.readFromFile(file);
            } catch (ParseException pe) {
                throw new RuntimeException("Could not parse the cached results");
            }
        }

        return relevantRegionToOptions;
    }

//    public static Map<JavaRegion, Set<Set<String>>> analyze(String programName, String[] args, String sdgFile, String entryPoint, List<String> features) throws IOException, ParseException {
//        Map<JavaRegion, Set<Set<String>>> results = ProgramAnalysis.analyze(programName, args);
//
//        if(results != null) {
//            return results;
//        }
//
//        String criterionFormat = "booleanValue()";
//        List<String> criterionLabels = Slicer.getCriterionLabel(sdgFile, entryPoint, criterionFormat);
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<Set<String>>> relevantRegionToOptions = Slicer.analyze(sdgFile, featuresToLabels);
//
//        if(Options.checkIfSave()) {
//            ProgramAnalysis.writeToFile(programName, relevantRegionToOptions);
//        }
//
//        return relevantRegionToOptions;
//    }

    public static Map<JavaRegion, Set<Set<String>>> analyze(String programName, String[] args, String database, String program) throws IOException, ParseException {
        Map<JavaRegion, Set<Set<String>>> results = ProgramAnalysis.analyze(programName, args);

        if(results != null) {
            return results;
        }

        Map<JavaRegion, Set<Set<String>>> relevantRegionToOptions = ProgramAnalysis.analyze(database, program);

        if(Options.checkIfSave()) {
            ProgramAnalysis.writeToFile(programName, relevantRegionToOptions);
        }

        return relevantRegionToOptions;
    }

    public static Map<JavaRegion, Set<Set<String>>> analyze(String database, String program) throws ParseException {
        // This is hardcode to get the output of Lotrack
        List<String> fields = new ArrayList<>();
        fields.add(ProgramAnalysis.PACKAGE);
        fields.add(ProgramAnalysis.CLASS);
        fields.add(ProgramAnalysis.METHOD);
//        fields.add(ProgramAnalysis.JAVA_LINE_NO);
        fields.add(ProgramAnalysis.JIMPLE_LINE_NO);
        fields.add(ProgramAnalysis.CONSTRAINT);
        fields.add(ProgramAnalysis.CONSTRAINT_PRETTY);
        fields.add(ProgramAnalysis.BYTECODE_INDEXES);
        fields.add(ProgramAnalysis.METHOD_BYTECODE_SIGNATURE_JOANA_STYLE);
        fields.add(ProgramAnalysis.USED_TERMS);

        List<String> sortBy = new ArrayList<>();
        sortBy.add(ProgramAnalysis.PACKAGE);
        sortBy.add(ProgramAnalysis.CLASS);
        sortBy.add(ProgramAnalysis.METHOD_BYTECODE_SIGNATURE_JOANA_STYLE);
        sortBy.add(ProgramAnalysis.METHOD);
        sortBy.add(ProgramAnalysis.BYTECODE_INDEXES);

        ScalaMongoDriverConnector.connect(database);
        List<String> queryResult = ScalaMongoDriverConnector.findProjectionAscending(program, fields, sortBy);
        ScalaMongoDriverConnector.close();

        Map<JavaRegion, Set<Set<String>>> regionsToOptions = new HashMap<>();
        String currentPackage = "";
        String currentClass = "";
        String currentMethod = "";
        JavaRegion currentJavaRegion = null;
        int currentBytecodeIndex = 0;
        Set<String> currentUsedTerms = new HashSet<>();
        JSONParser parser = new JSONParser();

        for(String result : queryResult) {
            JSONObject entry = (JSONObject) parser.parse(result);
            List<String> usedTerms = (List<String>) entry.get(ProgramAnalysis.USED_TERMS);
            List<Long> entryBytecodeIndexes = (List<Long>) entry.get(ProgramAnalysis.BYTECODE_INDEXES);

            String entryPackage = (String) entry.get(ProgramAnalysis.PACKAGE);
            String entryClass = (String) entry.get(ProgramAnalysis.CLASS);

            if(entryClass.contains(".")) {
                entryClass = entryClass.substring(entryClass.lastIndexOf(".") + 1);
            }

            String entryMethod = (String) entry.get(ProgramAnalysis.METHOD_BYTECODE_SIGNATURE_JOANA_STYLE);
            entryMethod = entryMethod.substring(entryMethod.lastIndexOf(".") + 1);

//            // TODO ignore regions in main method
//            if(entryMethod.contains("main")) {
//                continue;
//            }

            int entryBytecodeIndex = Math.toIntExact(entryBytecodeIndexes.get(entryBytecodeIndexes.indexOf(Collections.min(entryBytecodeIndexes))));

            if(entryBytecodeIndex < 0) {
                continue;
            }

            if(!currentPackage.equals(entryPackage) || !currentClass.equals(entryClass) || !currentMethod.equals(entryMethod)) {
                currentBytecodeIndex = 0;
                currentUsedTerms = new HashSet<>();
                currentUsedTerms.addAll(usedTerms);
                currentPackage = entryPackage;
                currentClass = entryClass;
                currentMethod = entryMethod;

                // TODO check this change that we do not want to start instrumenting at the beginning of a method
                continue;
            }

            if(usedTerms.size() == 1 && (usedTerms.contains("true") || usedTerms.contains("false"))) {
                currentUsedTerms = new HashSet<>();
                currentBytecodeIndex = Math.toIntExact(entryBytecodeIndexes.get(entryBytecodeIndexes.indexOf(Collections.min(entryBytecodeIndexes))));
                continue;
            }

            if(!currentUsedTerms.containsAll(usedTerms)) {
                currentUsedTerms.addAll(usedTerms);

                if(currentJavaRegion != null && currentJavaRegion.getRegionPackage().equals(entryPackage)
                        && currentJavaRegion.getRegionClass().equals(entryClass)
                        && currentJavaRegion.getRegionMethod().equals(entryMethod)
                        && currentJavaRegion.getStartBytecodeIndex() == currentBytecodeIndex) {
                    regionsToOptions.get(currentJavaRegion).add(currentUsedTerms);
                }
                else {
                    currentJavaRegion = new JavaRegion(entryPackage, entryClass, entryMethod, currentBytecodeIndex);

                    String constraint = (String) entry.get(ProgramAnalysis.CONSTRAINT_PRETTY);
                    constraint = ProgramAnalysis.replaceConstraintWithUsedTerms(constraint, new ArrayList<>(currentUsedTerms));
                    List<String> conjunctions = ProgramAnalysis.getConjunctions(constraint);

                    Set<Set<String>> constraints = new HashSet<>();

                    for(String conjunction : conjunctions) {
                        Set<String> constraintSet = ProgramAnalysis.getConstraintSet(conjunction, new ArrayList<>(currentUsedTerms));
                        constraints.add(constraintSet);
                    }

                    regionsToOptions.put(currentJavaRegion, constraints);
                    System.out.println(currentJavaRegion.getRegionClass() + " " + currentJavaRegion.getRegionMethod() + " " + currentJavaRegion.getStartBytecodeIndex() + " with " + currentUsedTerms);
                }
            }

            currentBytecodeIndex = Math.toIntExact(entryBytecodeIndexes.get(entryBytecodeIndexes.indexOf(Collections.min(entryBytecodeIndexes))));
        }

//        Set<JavaRegion> regionsWithSameFeatureInAllInstructions = new HashSet<>();
//
//        for(JavaRegion javaRegion : regionsToOptions.keySet()) {
//            List<String> regionUsedTerms = new ArrayList<>();
//
//            for(String result : queryResult) {
//                JSONObject entry = (JSONObject) parser.parse(result);
//                String entryPackage = (String) entry.get(ProgramAnalysis.PACKAGE);
//                String entryClass = (String) entry.get(ProgramAnalysis.CLASS);
//
//                if(entryClass.contains(".")) {
//                    entryClass = entryClass.substring(entryClass.lastIndexOf(".") + 1);
//                }
//
//                String entryMethod = (String) entry.get(ProgramAnalysis.METHOD);
//
//                if(!javaRegion.getRegionPackage().equals(entryPackage) || !javaRegion.getRegionClass().equals(entryClass) || !javaRegion.getRegionMethod().equals(entryMethod)) {
//                    if(!regionUsedTerms.isEmpty()) {
////                        System.out.println(javaRegion.getRegionMethod());
////                        regionsWithSameFeatureInAllInstructions.add(javaRegion);
////                        regionUsedTerms = new ArrayList<>();
//                        break;
//                    }
//
//                    continue;
//                }
//
//                if(regionUsedTerms.isEmpty()) {
//                    regionUsedTerms = (List<String>) entry.get(ProgramAnalysis.USED_TERMS);
//
//                    if(regionUsedTerms.size() == 1 && (regionUsedTerms.contains("true") || regionUsedTerms.contains("false"))) {
//                        regionUsedTerms = new ArrayList<>();
//                        break;
//                    }
//                    else {
//                        continue;
//                    }
//                }
//
//                List<String> usedTerms = (List<String>) entry.get(ProgramAnalysis.USED_TERMS);
//
//                if(!regionUsedTerms.equals(usedTerms)) {
//                    regionUsedTerms = new ArrayList<>();
//                    break;
//                }
//            }
//
//            if(!regionUsedTerms.isEmpty()) {
//                System.out.println(javaRegion.getRegionMethod());
//                regionsWithSameFeatureInAllInstructions.add(javaRegion);
//            }
//
//        }
//
//        for(JavaRegion javaRegion : regionsWithSameFeatureInAllInstructions) {
//            regionsToOptions.remove(javaRegion);
//        }

        return regionsToOptions;
    }

    private static void writeToFile(String programName, Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = ProgramAnalysis.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        List<DecisionAndOptions> decisionsAndOptions = new ArrayList<>();

        for(Map.Entry<JavaRegion, Set<Set<String>>> regionToOptionsSet : relevantRegionsToOptions.entrySet()) {
            DecisionAndOptions decisionAndOptions = new DecisionAndOptions(regionToOptionsSet.getKey(), regionToOptionsSet.getValue());
            decisionsAndOptions.add(decisionAndOptions);
        }

        mapper.writeValue(file, decisionsAndOptions);
    }

    private static Map<JavaRegion, Set<Set<String>>> readFromFile(File file) throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        List<DecisionAndOptions> results = mapper.readValue(file, new TypeReference<List<DecisionAndOptions>>() {
        });
        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = new HashMap<>();

        for(DecisionAndOptions result : results) {
            decisionsToOptionsSet.put(result.getRegion(), result.getOptions());
        }

        return decisionsToOptionsSet;

//        JSONParser parser = new JSONParser();
//        JSONObject result = (JSONObject) parser.parse(new FileReader(file));
//
//        Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions = new HashMap<>();
//
//        JSONArray regions = (JSONArray) result.get(ANALYSIS);
//
//        for(Object entry : regions) {
//            JSONObject regionResult = (JSONObject) entry;
//
//            String id = (String) regionResult.get(ProgramAnalysis.ID);
//            String regionPackage = (String) regionResult.get(ProgramAnalysis.REGION_PACKAGE);
//            String regionClass = (String) regionResult.get(ProgramAnalysis.REGION_CLASS);
//            String regionMethod = (String) regionResult.get(ProgramAnalysis.REGION_METHOD);
////            int regionJavaLineNumber = (int) (long) regionResult.get(ProgramAnalysis.REGION_JAVA_LINE_NUMBER);
//            int startBytecodeIndex = (int) (long) regionResult.get(ProgramAnalysis.START_BYTECODE_INDEX);
////            int endBytecodeIndex = (int) (long) regionResult.get(ProgramAnalysis.END_BYTCODE_INDEX);
//
//            JavaRegion javaRegion = new JavaRegion(id, regionPackage, regionClass, regionMethod, startBytecodeIndex);
////            javaRegion.setJavaLineNubmer(regionJavaLineNumber);
//
//            Set<String> options = new HashSet<>();
//            JSONArray optionsResult = (JSONArray) regionResult.get(ProgramAnalysis.OPTIONS);
//
//            for(Object optionResult : optionsResult) {
//                options.add((String) optionResult);
//            }
//
//            relevantRegionsToOptions.put(javaRegion, options);
//        }
//
//        return relevantRegionsToOptions;
    }

    public static String replaceConstraintWithUsedTerms(String constraint, List<String> usedTerms) {
        for(String usedTerm : usedTerms) {
            constraint = constraint.replaceAll(usedTerm + "(_[a-zA-Z]*)?", usedTerm);
        }

        return constraint;
    }

    public static List<String> getConjunctions(String constraint) {
        if(!constraint.startsWith("(") && !constraint.endsWith(")")) {
            constraint = "(" + constraint + ")";
        }

        List<String> conjunctions = new ArrayList<>();

        StringBuilder conjunction = new StringBuilder();
        int parenthesisCount = 0;

        for(int i = 0; i < constraint.length(); i++) {
            char currentChar = constraint.charAt(i);
            conjunction.append(currentChar);

            if(constraint.charAt(i) == '(') {
                parenthesisCount++;
            }
            else if(constraint.charAt(i) == ')') {
                parenthesisCount--;
            }
            else {
                continue;
            }

            if(parenthesisCount == 0) {
                conjunctions.add(conjunction.toString());
                conjunction = new StringBuilder();
            }

        }

        for(String conj : conjunctions) {
            int openParenthesisCount = 0;

            for(int i = 0; i < conj.length(); i++) {
                char currentChar = constraint.charAt(i);
                conjunction.append(currentChar);

                if(constraint.charAt(i) == '(') {
                    openParenthesisCount++;

                    if(openParenthesisCount > 2) {
                        throw new RuntimeException(conj + " is not a valid conjunction");
                    }
                }
                else if(constraint.charAt(i) == ')') {
                    openParenthesisCount--;
                }
            }
        }

        return conjunctions;
    }

    public static Set<String> getConstraintSet(String constraint, List<String> usedTerms) {
        Set<String> res = new HashSet<>();

        String[] terms = constraint.split(" ");

        for(int i = 0; i < terms.length; i++) {
            for(String usedTerm : usedTerms) {
                if(terms[i].contains(usedTerm)) {
                    res.add(usedTerm);
                }
            }
        }

        return res;
    }

}
