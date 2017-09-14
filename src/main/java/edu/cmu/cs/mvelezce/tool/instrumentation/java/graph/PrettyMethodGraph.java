package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrettyMethodGraph extends MethodGraph {

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
