package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.BaseRegionInstrumenter;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrettyMethodGraph extends MethodGraph {

    public static final String DOT_DOT = ".dot";

    public void saveDotFile(String programName, String className, String methodName) throws IOException {
        String dotString = this.toDotStringVerbose(methodName);
        File file = new File(BaseRegionInstrumenter.DIRECTORY + "/" + programName + "/" + className + "/" + methodName
                + PrettyMethodGraph.DOT_DOT);
        file.getParentFile().mkdirs();

        PrintWriter writer = new PrintWriter(BaseRegionInstrumenter.DIRECTORY + "/" + programName + "/" + className
                + "/" + methodName + PrettyMethodGraph.DOT_DOT);

        writer.println(dotString);
        writer.flush();
        writer.close();


//        BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        BufferedWriter writer = new BufferedWriter(new FileWriter(this.getSrcDir() + "/sources.txt"));
//        String string;
//
//        while ((string = inputReader.readLine()) != null) {
//            writer.write(string);
//            writer.write("\n");
//        }
//
//        writer.close();
//
//        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//
//        while ((string = errorReader.readLine()) != null) {
//            System.out.println(string);
//        }


    }

    public void savePdfFile() {
//        try {
//            String[] command = {"find", this.getSrcDir(), "-name", "*.java"};
//            System.out.println(Arrays.toString(command));
//            Process process = Runtime.getRuntime().exec(command);
//
//            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            BufferedWriter writer = new BufferedWriter(new FileWriter(this.getSrcDir() + "/sources.txt"));
//            String string;
//
//            while ((string = inputReader.readLine()) != null) {
//                writer.write(string);
//                writer.write("\n");
//            }
//
//            writer.close();
//
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//
//            while ((string = errorReader.readLine()) != null) {
//                System.out.println(string);
//            }
//
//            process.waitFor();
//        } catch (IOException | InterruptedException ie) {
//            ie.printStackTrace();
//        }
    }

    public String toDotStringVerbose(String methodName) {
        Set<MethodBlock> blocks = this.getBlocks();
        Set<PrettyMethodBlock> prettyBlocks = new HashSet<>();


        for(MethodBlock methodBlock : blocks) {
            if(methodBlock == this.getEntryBlock() || methodBlock == this.getExitBlock()) {
                continue;
            }

            prettyBlocks.add((PrettyMethodBlock) methodBlock);
        }


        StringBuilder dotString = new StringBuilder("digraph " + methodName + " {\n");
        dotString.append("node [shape=record];\n");

        for(PrettyMethodBlock prettyMethodBlock : prettyBlocks) {
            dotString.append(prettyMethodBlock.getID());
            dotString.append(" [label=\"");

            List<String> prettyInstructions = prettyMethodBlock.getPrettyInstructions();

            for(int i = 0; i < prettyInstructions.size(); i++) {
                String instruction = prettyInstructions.get(i);
                instruction = instruction.replace("\"", "\\\"");
                dotString.append(instruction);
                dotString.append("\\l");
            }

            dotString.append("\"];\n");
        }

        dotString.append(this.getEntryBlock().getID());
        dotString.append(";\n");
        dotString.append(this.getExitBlock().getID());
        dotString.append(";\n");

        for(MethodBlock methodBlock : this.getBlocks()) {
            for(MethodBlock successor : methodBlock.getSuccessors()) {
                dotString.append(methodBlock.getID());
                dotString.append(" -> ");
                dotString.append(successor.getID());
                dotString.append(";\n");
            }
        }

        dotString.append("}");

        return dotString.toString();
    }

}
