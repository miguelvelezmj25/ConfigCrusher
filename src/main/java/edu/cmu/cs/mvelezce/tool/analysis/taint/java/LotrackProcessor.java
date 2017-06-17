package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * Created by mvelezce on 4/5/17.
 */
// TODO how to create regions if there are execptions that the bytecode goes back to -1?
// TODO this is not correct
public class LotrackProcessor {
//    public static final String PACKAGE = "Package";
//    public static final String CLASS = "Class";
//    public static final String METHOD = "Method";
//    public static final String JAVA_LINE_NO = "JavaLineNo";
//    public static final String JIMPLE_LINE_NO = "JimpleLineNo";
//    public static final String CONSTRAINT = "Constraint";
//    public static final String CONSTRAINT_PRETTY = "ConstraintPretty";
//    public static final String BYTECODE_INDEXES = "bytecodeIndexes";
//    public static final String METHOD_BYTECODE_SIGNATURE_JOANA_STYLE = "methodBytecodeSignatureJoanaStyle";
//    public static final String USED_TERMS = "usedTerms";
//
//    public static final String LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL = "_";

    public static Map<JavaRegion, Set<String>> getRegionsToOptions(String database, String program) throws ParseException {
        return null;
//        // This is hardcode to get the output of Lotrack
//        List<String> fields = new ArrayList<>();
//        fields.add(LotrackProcessor.PACKAGE);
//        fields.add(LotrackProcessor.CLASS);
//        fields.add(LotrackProcessor.METHOD);
//        fields.add(LotrackProcessor.JAVA_LINE_NO);
//        fields.add(LotrackProcessor.JIMPLE_LINE_NO);
//        fields.add(LotrackProcessor.CONSTRAINT);
//        fields.add(LotrackProcessor.CONSTRAINT_PRETTY);
//        fields.add(LotrackProcessor.BYTECODE_INDEXES);
//        fields.add(LotrackProcessor.METHOD_BYTECODE_SIGNATURE_JOANA_STYLE);
//        fields.add(LotrackProcessor.USED_TERMS);
//
//        List<String> sortBy = new ArrayList<>();
//        sortBy.add(LotrackProcessor.PACKAGE);
//        sortBy.add(LotrackProcessor.CLASS);
//        sortBy.add(LotrackProcessor.METHOD);
////        sortBy.add(LotrackProcessor.JIMPLE_LINE_NO);
//        sortBy.add(LotrackProcessor.BYTECODE_INDEXES);
//
//        ScalaMongoDriverConnector.connect(database);
//        List<String> queryResult = ScalaMongoDriverConnector.findProjectionAscending(program, fields, sortBy);
//        ScalaMongoDriverConnector.close();
//
//        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
//        String currentPackage = "";
//        String currentClass = "";
//        String currentMethod = "";
//        JavaRegion currentJavaRegion = null;
//        Set<String> currentUsedTerms = new HashSet<>();
////        int currentJimpleLine = -10;
////        int currentByteCodeIndex = -10;
//        JSONParser parser = new JSONParser();
//
//        for(String result : queryResult) {
//            JSONObject entry = (JSONObject) parser.parse(result);
//            List<String> usedTerms = (List<String>) entry.get(LotrackProcessor.USED_TERMS);
//
//            if(usedTerms.size() == 1 && (usedTerms.contains("true") || usedTerms.contains("false"))) {
//                if(currentJavaRegion != null) {
//                    regionsToOptions.put(currentJavaRegion, currentUsedTerms);
//                    System.out.println(currentJavaRegion.getRegionClass() + " " + currentJavaRegion.getRegionMethod() + " " + currentJavaRegion.getJavaLineNubmer() + " with " + currentUsedTerms);
//                }
//
//                currentJavaRegion = null;
//                currentUsedTerms = new HashSet<>();
//                continue;
//            }
//
//            String entryPackage = (String) entry.get(LotrackProcessor.PACKAGE);
//            String entryClass = (String) entry.get(LotrackProcessor.CLASS);
//            String entryMethod = (String) entry.get(LotrackProcessor.METHOD);
//
//            if(!currentPackage.equals(entryPackage) ||!currentClass.equals(entryClass) || !currentMethod.equals(entryMethod)) {
//                currentUsedTerms = new HashSet<>();
//                currentPackage = entryPackage;
//                currentClass = entryClass;
//                currentMethod = entryMethod;
//            }
//
//            if(!currentUsedTerms.containsAll(usedTerms)) {
//                currentUsedTerms.addAll(usedTerms);
//
//                if(currentJavaRegion == null) {
//                    currentJavaRegion = new JavaRegion(entryPackage, entryClass, entryMethod);
//                    int entryJavaLineNumber = (int)(long) entry.get(LotrackProcessor.JAVA_LINE_NO);
//                    currentJavaRegion.setJavaLineNubmer(entryJavaLineNumber);
//                }
//
//
////                List<Long> bytecodeIndexes = (List<Long>) entry.get(LotrackProcessor.BYTECODE_INDEXES);
////                int minIndex = bytecodeIndexes.indexOf(Collections.min(bytecodeIndexes));
////
////                // TODO check the line number from asm to try to link it to the result of lotrack
////                JavaRegion partialJavaRegion = new JavaRegion(entryPackage, entryClass, entryMethod, (int) (bytecodeIndexes.get(minIndex) - 1));
////                regionsToOptions.put(partialJavaRegion, new HashSet<>(usedTerms));
////                System.out.println("region at " + (bytecodeIndexes.get(minIndex) - 1) + " with " + usedTerms);
//            }
//        }
//
//        return regionsToOptions;
    }

    public static Map<JavaRegion, Set<String>> filterBooleans(Map<JavaRegion, Set<String>> regionToOptions) {
        // These are language dependent since they can be writen with other capitalization
        Set<String> optionsToRemove = new HashSet<>();
        optionsToRemove.add("true");
        optionsToRemove.add("false");

        Map<JavaRegion, Set<String>> filteredMap = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : regionToOptions.entrySet()) {
            Set<String> options = entry.getValue();
            options.removeAll(optionsToRemove);
            filteredMap.put(entry.getKey(), options);
        }

        return filteredMap;
    }

    public static Map<JavaRegion, Set<String>> filterRegionsNoOptions(Map<JavaRegion, Set<String>> regionToOptions) {
        Map<JavaRegion, Set<String>> filteredMap = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : regionToOptions.entrySet()) {
            if(!entry.getValue().isEmpty()) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }

        return filteredMap;
    }

}
