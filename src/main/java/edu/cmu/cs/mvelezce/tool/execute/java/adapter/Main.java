package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import java.io.IOException;

public interface Main {

    public void logExecution() throws IOException;

    public void execute(String mainClass, String[] sleepArgs) throws Exception;

}
