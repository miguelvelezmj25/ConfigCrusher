package edu.cmu.cs.mvelezce.analysis.taint.java;

import edu.cmu.cs.mvelezce.analysis.taint.Region;
import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by mvelezce on 4/5/17.
 */
public class LotrackProcessor {

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

    public static Map<Region, Set<String>> getRegionsToOptions(String database, String program) throws NoSuchFieldException {
        // This is hardcode to get the output of Lotrack
        List<String> fields = new ArrayList<>();
        fields.add(LotrackProcessor.PACKAGE);
        fields.add(LotrackProcessor.CLASS);
        fields.add(LotrackProcessor.METHOD);
        fields.add(LotrackProcessor.JAVA_LINE_NO);
        fields.add(LotrackProcessor.JIMPLE_LINE_NO);
        fields.add(LotrackProcessor.CONSTRAINT);
        fields.add(LotrackProcessor.CONSTRAINT_PRETTY);
        fields.add(LotrackProcessor.BYTECODE_INDEXES);
        fields.add(LotrackProcessor.METHOD_BYTECODE_SIGNATURE_JOANA_STYLE);
        fields.add(LotrackProcessor.USED_TERMS);

        List<String> sortBy = new ArrayList<>();
        fields.add(LotrackProcessor.PACKAGE);
        fields.add(LotrackProcessor.CLASS);
        fields.add(LotrackProcessor.METHOD);
        fields.add(LotrackProcessor.JIMPLE_LINE_NO);

        ScalaMongoDriverConnector.connect(database);
        List<String> queryResult = ScalaMongoDriverConnector.queryAscending(program, fields, sortBy);
        ScalaMongoDriverConnector.close();
        Map<Region, Set<String>> regionsToOptions = new HashedMap<>();

        for(String result : queryResult) {
            JSONObject JSONResult = new JSONObject(result);
            Set<String> options = new HashSet<>();

            if(JSONResult.has(LotrackProcessor.USED_TERMS)) {
                for(Object string : JSONResult.getJSONArray(LotrackProcessor.USED_TERMS).toList()) {
                    options.add(string.toString());
                }
            }
            else if(JSONResult.has(LotrackProcessor.CONSTRAINT)) {
                // Be careful that this is imprecise since the constraints can be very large and does not fit in the db field
                String[] constraints = JSONResult.getString(LotrackProcessor.CONSTRAINT).split(" ");

                for(String constraint : constraints) {
                    constraint = constraint.replaceAll("[()^|!=]", "");
                    if(constraint.isEmpty() || StringUtils.isNumeric(constraint)) {
                        continue;
                    }

                    if(constraint.contains(LotrackProcessor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)) {
                        constraint = constraint.split(LotrackProcessor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)[0];
                    }

                    // Because the constraint gotten from Lotrack might be too long
                    if(constraint.contains(".")) {
                        continue;
                    }

                    options.add(constraint);
                }
            }
            else {
                throw new NoSuchFieldException("The query result does not have neither a " + LotrackProcessor.USED_TERMS + " or " + LotrackProcessor.CONSTRAINT + " fields");
            }


            Region currentRegion = new Region(JSONResult.get(LotrackProcessor.PACKAGE).toString(), JSONResult.get(LotrackProcessor.CLASS).toString(), JSONResult.get(LotrackProcessor.METHOD).toString());

            if(regionsToOptions.containsKey(currentRegion)) {
                Set<String> oldOptions = regionsToOptions.get(currentRegion);
                options.addAll(oldOptions);
            }
//
            regionsToOptions.put(currentRegion, options);
        }

        for(Map.Entry<Region, Set<String>> entry : regionsToOptions.entrySet()) {
            if(entry.getValue().size() > 4) {
                int a = 0;
            }
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
