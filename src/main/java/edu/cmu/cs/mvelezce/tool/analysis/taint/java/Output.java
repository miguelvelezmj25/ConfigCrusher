package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

/**
 * Created by mvelezce on 6/15/17.
 */
public class Output {

    public static final String PACKAGE = "Package";
    public static final String CLASS = "Class";
    public static final String METHOD = "Method";
    public static final String JAVA_LINE_NO = "JavaLineNo";
    public static final String CONSTRAINT = "Constraint";
    public static final String CONSTRAINT_PRETTY = "ConstraintPretty";
    public static final String BYTECODE_INDEXES = "bytecodeIndexes";
    public static final String USED_TERMS = "usedTerms";

    public static void main(String[] args) throws NoSuchFieldException, ParseException {

//        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src/";
//        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/jarchivelib/src/main/java/";
//        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/java-lame/src/main/java/";
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic/src/main/java/";
//        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/src/";
//        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/gpl/src/";
        String[] extensions = {"java"};

        Collection<File> files = FileUtils.listFiles(new File(root), extensions, true);
        List<String> dbEntries = getDBEntries("loadtime", "Tests");

        for(File file : files) {
            Map<Integer, String> linesToConstraints = new HashMap<>();

            for(String result : dbEntries) {
                JSONParser parser = new JSONParser();
                JSONObject entry = (JSONObject) parser.parse(result);
                String c = (String) entry.get(Output.CLASS);

                if(!c.contains(".")) {
                    continue;
                }

                c = c.substring(c.lastIndexOf(".") + 1);
                if(!file.getName().equals(c + ".java")) {
                    continue;
                }

                String constraint = (String) entry.get(Output.CONSTRAINT);

                if(constraint.equals("true") /*|| constraint.equals("false")*/) {
                    continue;
                }

                try {
                    FileInputStream fstream = new FileInputStream(root + file.getPath().replace(root, ""));
                    DataInputStream in = new DataInputStream(fstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String strLine = br.readLine();
                    in.close();

                    if(!strLine.contains((String) entry.get(Output.PACKAGE))) {
                        continue;
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }

                int javaLineNumber = (int) (long) entry.get(Output.JAVA_LINE_NO);
                List<String> usedTerms = (List<String>) entry.get(Output.USED_TERMS);
                linesToConstraints.put(javaLineNumber, constraint + " " + usedTerms.toString());
            }

            if(linesToConstraints.isEmpty()) {
                continue;
            }

            StringBuilder lotrackedFile = new StringBuilder();

            try {
                FileInputStream fstream = new FileInputStream(root + file.getPath().replace(root, ""));
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                int lineNumber = 1;

                while ((strLine = br.readLine()) != null) {
//                    lotrackedFile.append(lineNumber).append("    ");
                    lotrackedFile.append(strLine).append("    ");

                    if(linesToConstraints.containsKey(lineNumber)) {
                        lotrackedFile.append("// LT CONSTRAINT ").append(linesToConstraints.get(lineNumber));
                    }

                    lotrackedFile.append("\n");
                    lineNumber++;
                }

                in.close();

                try (PrintWriter out = new PrintWriter(file.getAbsolutePath().replace(".java", "") + "LOTRACK.java")) {
                    out.print(lotrackedFile.toString());

                    System.out.println("Done with " + file.getName());
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public static List<String> getDBEntries(String database, String program) throws NoSuchFieldException, ParseException {
        // This is hardcode to get the output of Lotrack
        List<String> fields = new ArrayList<>();
        fields.add(Output.PACKAGE);
        fields.add(Output.CLASS);
        fields.add(Output.METHOD);
        fields.add(Output.JAVA_LINE_NO);
        fields.add(Output.CONSTRAINT);
        fields.add(Output.CONSTRAINT_PRETTY);
        fields.add(Output.BYTECODE_INDEXES);
        fields.add(Output.USED_TERMS);

        List<String> sortBy = new ArrayList<>();
        sortBy.add(Output.PACKAGE);
        sortBy.add(Output.CLASS);
        sortBy.add(Output.METHOD);
        sortBy.add(Output.JAVA_LINE_NO);

        ScalaMongoDriverConnector.connect(database);
        List<String> queryResult = ScalaMongoDriverConnector.findProjectionAscending(program, fields, sortBy);
        ScalaMongoDriverConnector.close();

        return queryResult;
    }

}
