package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import java.util.Set;

public interface Adapter {

    public void execute(Set<String> configuration);

    public String[] configurationAsMainArguments(Set<String> configuration);

    public Set<String> configurationAsSet(String[] configuration);

}
