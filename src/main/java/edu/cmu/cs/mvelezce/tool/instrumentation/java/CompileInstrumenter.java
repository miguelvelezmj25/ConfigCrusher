package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CompileInstrumenter extends BaseInstrumenter {

//  public static final String M2_DIR = BaseAdapter.USER_HOME + "/.m2/repository";

  public CompileInstrumenter(String srcDir, String classDir) {
    this("", srcDir, classDir);
  }

  public CompileInstrumenter(String programName, String srcDir, String classDir) {
    super(programName, srcDir, classDir);
  }

  @Override
  public void instrument()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void compile() throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList();
    builder.command(commandList);
    builder.directory(new File(this.getSrcDir()));
    Process process = builder.start();

    edu.cmu.cs.mvelezce.tool.Helper.processOutput(process);
    edu.cmu.cs.mvelezce.tool.Helper.processError(process);

    process.waitFor();
  }

  private List<String> buildCommandAsList() {
    List<String> commands = new ArrayList<>();

    commands.add("mvn");
    commands.add("clean");
    commands.add("package");
    commands.add("-DskipTests");

    return commands;
  }

}
