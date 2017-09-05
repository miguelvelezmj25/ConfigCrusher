package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

public abstract class BaseInstrumenter implements Instrumenter {
    private String srcDir;
    private String classDir;
    private Set<JavaRegion> regions;

    public BaseInstrumenter(String srcDir, String classDir, Set<JavaRegion> regions) {
        this.srcDir = srcDir;
        this.classDir = classDir;
        this.regions = regions;
    }

    @Override
    public void instrument(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        Options.getCommandLine(args);

        if(Options.checkIfDeleteResult()) {
            this.compile();
        }

        if(Options.checkIfSave()) {
            this.instrument();
        }
    }

    private void compile() {
        this.writeFilesToCompile();

        try {
            String[] command = new String[]{"javac", //"-cp",
//                    "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/lib/*",
                    "-d", this.classDir, "@" + this.srcDir + "sources.txt"};
            System.out.println(Arrays.toString(command));
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String string;

            while(inputReader.readLine() != null) {
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            while ((string = errorReader.readLine()) != null) {
                System.out.println(string);
            }

            process.waitFor();
        }
        catch (IOException | InterruptedException ie) {
            ie.printStackTrace();
        }

    }

    private void writeFilesToCompile() {
        try {
            String[] command = {"find", this.srcDir, "-name", "*.java"};
            System.out.println(Arrays.toString(command));
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.srcDir + "sources.txt"));
            String string;

            while ((string = inputReader.readLine()) != null) {
                writer.write(string);
                writer.write("\n");
            }

            writer.close();

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            while ((string = errorReader.readLine()) != null) {
                System.out.println(string);
            }

            process.waitFor();
        }
        catch (IOException | InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public String getSrcDir() {
        return srcDir;
    }

    public String getClassDir() {
        return classDir;
    }

    public Set<JavaRegion> getRegions() {
        return regions;
    }
}
