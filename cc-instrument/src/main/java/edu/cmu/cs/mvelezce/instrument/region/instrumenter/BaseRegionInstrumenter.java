package edu.cmu.cs.mvelezce.instrument.region.instrumenter;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrumenter.instrument.BaseInstrumenter;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public abstract class BaseRegionInstrumenter<T> extends BaseInstrumenter {

  private final Set<String> options;
  private final String mainClass;
  private final Map<JavaRegion, T> regionsToData;

  public BaseRegionInstrumenter(
      String programName,
      String mainClass,
      String srcDir,
      String classDir,
      Set<String> options,
      Map<JavaRegion, T> regionsToData) {
    super(programName, srcDir, classDir);

    System.err.println(
        "Why is that there was a method transformer to explicitly add a variable before a return?");

    this.options = options;
    this.mainClass = mainClass;
    this.regionsToData = regionsToData;
  }

  public void instrument(String[] args)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException,
          InterruptedException {
    Options.getCommandLine(args);

    if (Options.checkIfDeleteResult()) {
      this.compile();
    }

    if (Options.checkIfSave()) {
      this.instrument();
      this.writeToFile(this.regionsToData);
    }
  }

  protected Set<String> getOptions() {
    return options;
  }

  protected String getMainClass() {
    return mainClass;
  }

  protected Map<JavaRegion, T> getRegionsToData() {
    return regionsToData;
  }

  protected abstract void writeToFile(Map<JavaRegion, T> regionsToData) throws IOException;

  protected abstract Map<JavaRegion, T> readFromFile(File file) throws IOException;

  public abstract Map<JavaRegion, T> getProcessedRegionsToData() throws IOException;
}
