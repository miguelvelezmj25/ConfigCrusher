package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

public abstract class BaseMain implements Main {

    private String programName;
    private String iteration;
    private String[] args;

    public BaseMain(String programName, String iteration, String[] args) {
        this.programName = programName;
        this.iteration = iteration;
        this.args = args;
    }

    public String getProgramName() {
        return programName;
    }

    public String getIteration() {
        return iteration;
    }

    public String[] getArgs() {
        return args;
    }
}
