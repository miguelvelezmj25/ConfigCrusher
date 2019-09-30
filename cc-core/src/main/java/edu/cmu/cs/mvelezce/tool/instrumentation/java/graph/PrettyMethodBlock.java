package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvelezce on 5/3/17.
 */
public class PrettyMethodBlock extends MethodBlock {

    private List<String> prettyInstructions = new ArrayList<>();

    public PrettyMethodBlock(String ID) {
        super(ID);
    }

    public List<String> getPrettyInstructions() {
        return this.prettyInstructions;
    }
}
