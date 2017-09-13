package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodBlockPretty extends MethodBlock {

    private List<String> pretyInstructions = new ArrayList<>();

    public MethodBlockPretty(String ID) {
        super(ID);
    }

    public List<String> getPretyInstructions() {
        return this.pretyInstructions;
    }
}
