package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public abstract class Adapter {

    public static final String MAIN = "main";
    public static final String CLASS_CONTAINER = "target/classes/";

    public abstract void execute(Set<String> configuration);

    // TODO how to do this better?
    public static String executeJavaProgram(String mainAdapter, String mainClass, String directory, String args) {
        String command = "java -cp " + directory + ":" + Adapter.CLASS_CONTAINER + " " + mainAdapter + " " + mainClass + " " + args;
        System.out.println(command);
        StringBuilder output = new StringBuilder();
        Process process;

        // TODO where to measure the entire program? This is very noisy!
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(output);
        return output.toString();
    }

}
