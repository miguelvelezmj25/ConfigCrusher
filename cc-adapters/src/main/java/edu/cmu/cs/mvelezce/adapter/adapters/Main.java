package edu.cmu.cs.mvelezce.adapter.adapters;

import java.io.IOException;

public interface Main {

  void logExecution() throws IOException;

  void execute(String mainClass, String[] sleepArgs);
}
