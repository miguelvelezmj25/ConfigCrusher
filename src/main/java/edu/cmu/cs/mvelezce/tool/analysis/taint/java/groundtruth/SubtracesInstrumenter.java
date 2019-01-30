package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.BaseInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Compiler;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SubtracesInstrumenter extends BaseInstrumenter {

  public static final String DIRECTORY = Options.DIRECTORY + "/analysis/spec/java/programs";

  public SubtracesInstrumenter(String programName, String srcDir, String classDir) {
    super(programName, srcDir, classDir);
  }

  @Override
  public void instrument(String args[])
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Options.getCommandLine(args);

    File outputFile = new File(DIRECTORY + "/" + this.getProgramName());
    Options.checkIfDeleteResult(outputFile);

    if (outputFile.exists()) {
      throw new UnsupportedOperationException("Implement");
//      Collection<File> files = FileUtils.listFiles(outputFile, new String[]{"json"}, false);
//
//      if (files.size() != 1) {
//        throw new RuntimeException(
//            "We expected to find 1 file in the directory, but that is not the case "
//                + outputFile);
//      }
//
//      return;
    }

    if (Options.checkIfDeleteResult()) {
      this.compile();
    }

    if (Options.checkIfSave()) {
      this.instrument();
      throw new UnsupportedOperationException("Implement what we want to save");
    }
  }

  @Override
  public void instrument()
      throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    MethodTransformer transformer = new SubtracesMethodTransformer.Builder(this.getProgramName(),
        this.getClassDir())
        .build();
    transformer.transformMethods();
  }

  @Override
  public void compile() throws IOException, InterruptedException {
    Compiler.compile(this.getSrcDir());
  }
}
