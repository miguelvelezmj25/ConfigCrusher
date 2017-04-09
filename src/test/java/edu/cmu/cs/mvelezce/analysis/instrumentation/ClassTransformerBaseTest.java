package edu.cmu.cs.mvelezce.analysis.instrumentation;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by miguelvelez on 4/9/17.
 */
public class ClassTransformerBaseTest {

    protected static final String FILE_TO_INSTRUMENT_NAME = "edu/cmu/cs/mvelezce/analysis/instrumentation/DummyClass";

    protected static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        Process process;

        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(output);
        return output.toString();
    }

}