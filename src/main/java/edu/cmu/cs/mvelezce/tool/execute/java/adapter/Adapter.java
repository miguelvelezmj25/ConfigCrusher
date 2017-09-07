package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import java.io.IOException;
import java.util.Set;

public interface Adapter {

    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException;

    public void execute(Set<String> configuration) throws IOException, InterruptedException;

    public void execute(String mainAdapter, String[] args) throws InterruptedException, IOException;

    public String[] configurationAsMainArguments(Set<String> configuration);

    public Set<String> configurationAsSet(String[] configuration);

}
