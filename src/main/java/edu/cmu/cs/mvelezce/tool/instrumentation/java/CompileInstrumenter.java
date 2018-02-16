package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

import static edu.cmu.cs.mvelezce.tool.Options.USER_HOME;

public class CompileInstrumenter extends BaseInstrumenter {

    public static final String M2_DIR = USER_HOME + "/.m2/repository";
    public static final String sep = System.getProperty("path.separator");


    public CompileInstrumenter(String srcDir, String classDir) {
        this("", srcDir, classDir);
    }

    public CompileInstrumenter(String programName, String srcDir, String classDir) {
        super(programName, srcDir, classDir);
    }

    @Override
    public void instrument() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void compileFromSource() throws IOException, InterruptedException {
        this.writeFilesToCompile();

        try {
            File m2Dir = new File(CompileInstrumenter.M2_DIR);
            Collection<File> m2Files = FileUtils.listFiles(m2Dir, new String[]{"jar"}, true);

            StringBuilder cp = new StringBuilder();

            for(File jarFile : m2Files) {
                cp.append(jarFile);
                cp.append(CompileInstrumenter.sep);
            }

            cp.deleteCharAt(cp.length() - 1);

            String[] command = new String[]{"javac", "-cp", cp.toString(),
                    "-d", this.getClassDir(), "@" + this.getSrcDir() + "/sources.txt"};
            System.out.println(Arrays.toString(command));
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String string;

            while(inputReader.readLine() != null) {
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            while((string = errorReader.readLine()) != null) {
                System.out.println(string);
            }

            process.waitFor();
        } catch(IOException | InterruptedException ie) {
            ie.printStackTrace();
        }

    }

    private void writeFilesToCompile() throws IOException, InterruptedException {
        String[] command = {"find", this.getSrcDir(), "-name", "*.java"};
        System.out.println(Arrays.toString(command));
        Process process = Runtime.getRuntime().exec(command);

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.getSrcDir() + "/sources.txt"));
        String string;

        while((string = inputReader.readLine()) != null) {
            writer.write(string);
            writer.write("\n");
        }

        writer.close();

        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        while((string = errorReader.readLine()) != null) {
            System.out.println(string);
        }

        process.waitFor();
    }

}
