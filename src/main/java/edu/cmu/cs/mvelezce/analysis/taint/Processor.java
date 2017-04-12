package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by mvelezce on 4/5/17.
 */
public class Processor {

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

    public static Map<Region, Set<String>> getRegionsToOptions(String database, String program) {
        // This is hardcode to get the output of Lotrack
        List<String> fields = new ArrayList<>();
        fields.add(Processor.PACKAGE);
        fields.add(Processor.CLASS);
        fields.add(Processor.METHOD);
        fields.add(Processor.JAVA_LINE_NO);
        fields.add(Processor.JIMPLE_LINE_NO);
        fields.add(Processor.CONSTRAINT);
        fields.add(Processor.CONSTRAINT_PRETTY);
        fields.add(Processor.BYTECODE_INDEXES);
        fields.add(Processor.METHOD_BYTECODE_SIGNATURE_JOANA_STYLE);
        fields.add(Processor.USED_TERMS);

        List<String> sortBy = new ArrayList<>();
        fields.add(Processor.PACKAGE);
        fields.add(Processor.CLASS);
        fields.add(Processor.METHOD);
        fields.add(Processor.JIMPLE_LINE_NO);

        ScalaMongoDriverConnector.connect(database);
        List<String> queryResult = ScalaMongoDriverConnector.queryAscending(program, fields, sortBy);
        ScalaMongoDriverConnector.close();
        Map<Region, Set<String>> regionsToOptions = new HashedMap<>();

        for(String result : queryResult) {
            JSONObject JSONResult = new JSONObject(result);
            Set<String> options = new HashSet<>();

            if(JSONResult.has(Processor.USED_TERMS)) {
                for(Object string : JSONResult.getJSONArray(Processor.USED_TERMS).toList()) {
                    options.add(string.toString());
                }
            }
            else {
                // TODO with logic from below
            }

//            String[] constraints = result.get(Processor.CONSTRAINT).split(" ");
//            Set<String> options = new HashSet<>();
//
//            for(String constraint : constraints) {
//                constraint = constraint.replaceAll("[()^|!=]", "");
//                if(constraint.isEmpty() || StringUtils.isNumeric(constraint)) {
//                    continue;
//                }
//
//                if(constraint.contains(Processor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)) {
//                    constraint = constraint.split(Processor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)[0];
//                }
//
//                // Because the constraint gotten from Lotrack might be too long
//                if(constraint.contains(".")) {
//                    continue;
//                }
//
//                options.add(constraint);
//            }
//
            Region currentRegion = new Region(JSONResult.get(Processor.PACKAGE).toString(), JSONResult.get(Processor.CLASS).toString(), JSONResult.get(Processor.METHOD).toString());

            if(regionsToOptions.containsKey(currentRegion)) {
                Set<String> oldOptions = regionsToOptions.get(currentRegion);
                options.addAll(oldOptions);
            }
//
            regionsToOptions.put(currentRegion, options);
        }

        return regionsToOptions;
    }

    public static Map<Region, Set<String>> filterBooleans(Map<Region, Set<String>> regionToOptions) {
        // These are language dependent since they can be writen with other capitalization
        Set<String> optionsToRemove = new HashSet<>();
        optionsToRemove.add("true");
        optionsToRemove.add("false");

        Map<Region, Set<String>> filteredMap = new HashedMap<>();

        for(Map.Entry<Region, Set<String>> entry : regionToOptions.entrySet()) {
            Set<String> options = entry.getValue();
            options.removeAll(optionsToRemove);
            filteredMap.put(entry.getKey(), options);
        }

        return filteredMap;
    }

    public static Map<Region, Set<String>> filterRegionsNoOptions(Map<Region, Set<String>> regionToOptions) {
        Map<Region, Set<String>> filteredMap = new HashedMap<>();

        for(Map.Entry<Region, Set<String>> entry : regionToOptions.entrySet()) {
            if(!entry.getValue().isEmpty()) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }

        return filteredMap;
    }

}
