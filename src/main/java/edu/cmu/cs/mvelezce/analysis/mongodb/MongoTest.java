package edu.cmu.cs.mvelezce.analysis.mongodb;

import edu.cmu.cs.mvelezce.analysis.mapper.Region;
import edu.cmu.cs.mvelezce.mongo.connector.Casbah;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by mvelezce on 4/5/17.
 */
public class MongoTest {

    public static final String PACKAGE = "Package";
    public static final String CLASS = "Class";
    public static final String METHOD = "Method";
    public static final String JAVA_LINE_NO = "JavaLineNo";
    public static final String JIMPLE_LINE_NO = "JimpleLineNo";
    public static final String CONSTRAINT = "Constraint";
    public static final String CONSTRAINT_PRETTY = "ConstraintPretty";
    public static final String BYTECODE_INDEXES = "bytecodeIndexes";
    public static final String METHOD_BYTECODE_SIGNATURE_JOANA_STYLE = "methodBytecodeSignatureJoanaStyle";

    public static final String PLAYYPUS = "platypus";

    public static final String LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL = "_";

    public static void test() {
        List<String> fields = new ArrayList<>();
        fields.add(MongoTest.PACKAGE);
        fields.add(MongoTest.CLASS);
        fields.add(MongoTest.METHOD);
        fields.add(MongoTest.JAVA_LINE_NO);
        fields.add(MongoTest.JIMPLE_LINE_NO);
        fields.add(MongoTest.CONSTRAINT);
        fields.add(MongoTest.CONSTRAINT_PRETTY);
        fields.add(MongoTest.BYTECODE_INDEXES);
        fields.add(MongoTest.METHOD_BYTECODE_SIGNATURE_JOANA_STYLE);

        List<String> sortBy = new ArrayList<>();
        fields.add(MongoTest.PACKAGE);
        fields.add(MongoTest.CLASS);
        fields.add(MongoTest.METHOD);
        fields.add(MongoTest.JIMPLE_LINE_NO);

        List<Map<String, String>> queryResult = Casbah.connect(MongoTest.PLAYYPUS, fields, sortBy);
        Map<Region, Set<String>> regionsToOptions = new HashedMap<>();

        for(Map<String, String> result : queryResult) {
//            System.out.println(result.get(MongoTest.CONSTRAINT));
            String[] constraints = result.get(MongoTest.CONSTRAINT).split(" ");
            Set<String> options = new HashSet<>();

            for(String constraint : constraints) {
                constraint = constraint.replaceAll("[()^|!=]", "");
                if(constraint.isEmpty() || StringUtils.isNumeric(constraint)) {
                    continue;
                }

                if(constraint.contains(MongoTest.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)) {
                    constraint = constraint.split(MongoTest.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)[0];
                }

                // Because the constraint gotten from Lotrack might be too long
                if(constraint.contains(".")) {
                    continue;
                }

                options.add(constraint);
            }

            Region currentRegion = new Region(result.get(MongoTest.PACKAGE), result.get(MongoTest.CLASS), result.get(MongoTest.METHOD));

            if(regionsToOptions.containsKey(currentRegion)) {
                Set<String> oldOptions = regionsToOptions.get(currentRegion);
                options.addAll(oldOptions);
            }

            regionsToOptions.put(currentRegion, options);
        }

        for(Map.Entry<Region, Set<String>> entry : regionsToOptions.entrySet()) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
            System.out.println(entry.getValue());
        }

    }

//    public static Map<Region, Set<String>> filterBoleans(Map<Region, Set<String>> regionToOptions) {
//        Map<Region, Set<String>> filteredMap = new HashedMap<>();
//
//
//    }
}
