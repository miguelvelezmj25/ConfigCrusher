package edu.cmu.cs.mvelezce.analysis.mongodb;

import edu.cmu.cs.mvelezce.mongo.connector.Casbah;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mvelezce on 4/5/17.
 */
public class MongoTest {

    public static void test() {
        List<String> fields = new ArrayList<>();
        fields.add("Package");
        fields.add("Class");
        fields.add("Method");
        fields.add("JavaLineNo");
        fields.add("JimpleLineNo");
        fields.add("Constraint");
        fields.add("ConstraintPretty");
        fields.add("bytecodeIndexes");
        fields.add("methodBytecodeSignatureJoanaStyle");

        List<String> sortBy = new ArrayList<>();
        fields.add("Package");
        fields.add("Class");
        fields.add("Method");
        fields.add("JimpleLineNo");

        List<Map<String, String>> query = Casbah.connect("platypus", fields, sortBy);

        String currentMethod = "";
        for(Map<String, String> result : query) {
            String method = result.get("Method");

//            if(!method.contains("loadCommandFile")) {
//                continue;
//            }

//            if(method.contains("createCommandLine") || method.contains("appendFormatExtension") || method.contains("dummy") || method.contains("<init>") || method.contains("getRoot") || method.contains("notExecutedInCodeSection")) {
//                continue;
//            }
//
//            if(currentMethod.isEmpty()) {
//                currentMethod = method;
//            }
//
//            if(!result.get("Method").equals(currentMethod)) {
//                break;
//            }

//            if(result.get("Constraint").equals("true")) {
//                continue;
//            }

            for(Map.Entry<String, String> entry : result.entrySet()) {
//                String key = entry.getKey();

//                if(key.equals("JavaLineNo") || key.equals("Package") || key.equals("Class") || key.equals("Method") || key.equals("JimpleLineNo") || key.equals("Constraint")) {
                    System.out.println(entry.getKey() + ":" + entry.getValue());
//                }
            }
            System.out.println();
        }
    }
}
