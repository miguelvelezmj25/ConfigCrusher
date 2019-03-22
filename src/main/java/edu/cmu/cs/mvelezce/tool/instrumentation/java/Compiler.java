package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Helper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compiler {

  public static void compile(String dir) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = buildCommandAsList();
    builder.command(commandList);
    builder.directory(new File(dir));
    Process process = builder.start();

    Helper.processOutput(process);
    Helper.processError(process);

    process.waitFor();
  }

  private static List<String> buildCommandAsList() {
    List<String> commands = new ArrayList<>();

    commands.add("mvn");
    commands.add("clean");
    commands.add("compile");
    commands.add("-DskipTests");

    return commands;
  }
}
