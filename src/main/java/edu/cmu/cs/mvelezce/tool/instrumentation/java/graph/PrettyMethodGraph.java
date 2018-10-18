package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.BaseRegionInstrumenter;

import java.io.*;
import java.util.*;

public class PrettyMethodGraph extends MethodGraph {

    public static final String DOT_DOT = ".dot";
    public static final String DOT_PDF = ".pdf";

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
    }

    public void savePdfFile(String programName, String className, String methodName) throws IOException, InterruptedException {
        List<String> commandList = new ArrayList<>();
        commandList.add("dot");
        commandList.add("-Tpdf");
        commandList.add(BaseRegionInstrumenter.DIRECTORY + "/" + programName + "/" + className + "/" + methodName
                + PrettyMethodGraph.DOT_DOT);
        commandList.add("-o");
        commandList.add(BaseRegionInstrumenter.DIRECTORY + "/" + programName + "/" + className + "/" + methodName
                + PrettyMethodGraph.DOT_PDF);

        String[] command = new String[commandList.size()];
        command = commandList.toArray(command);
        System.out.println(Arrays.toString(command));

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String string;

        while((string = inputReader.readLine()) != null) {
            if(!string.isEmpty()) {
                output.append(string).append("\n");
            }
        }

        System.out.println(output);

        output = new StringBuilder();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        while((string = errorReader.readLine()) != null) {
            if(!string.isEmpty()) {
                output.append(string).append("\n");
            }
        }

        System.out.println(output);

        process.waitFor();
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
                instruction = instruction.replace("<", "\\<");
                instruction = instruction.replace(">", "\\>");
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

        Set<PrettyMethodBlock> instrumentedPrettyBlocks = new HashSet<>();

        for(PrettyMethodBlock prettyBlock : prettyBlocks) {
            for(String prettyInstruction : prettyBlock.getPrettyInstructions()) {
                // TODO do not hard code this
                if((prettyInstruction.contains("Regions") || prettyInstruction.contains("RegionsCounter"))
                        && (prettyInstruction.contains("enter") || prettyInstruction.contains("exit"))) {
                    instrumentedPrettyBlocks.add(prettyBlock);
                }
            }
        }

        for(PrettyMethodBlock prettyBlock : instrumentedPrettyBlocks) {
            dotString.append(prettyBlock.getID());
            dotString.append("[fontcolor=\"purple\", penwidth=3, color=\"purple\"];\n");
        }

        dotString.append("}");

        return dotString.toString();
    }

}
