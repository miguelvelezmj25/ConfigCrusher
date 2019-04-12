package edu.cmu.cs.mvelezce.tool.instrumentation.java.region;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.BaseInstrumenter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

public abstract class BaseRegionInstrumenter<T> extends BaseInstrumenter {

  public static final String DIRECTORY = Options.DIRECTORY + "/instrumentation/java/programs";

  private Map<JavaRegion, T> regionsToData;

  public BaseRegionInstrumenter(String programName, String classDir,
      Map<JavaRegion, T> regionsToData) {
    super(programName, null, classDir);
    this.regionsToData = regionsToData;
  }

  public BaseRegionInstrumenter(String programName) {
    this(programName, null, new HashMap<>());
  }

  @Override
  public void instrument(String[] args)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException, InterruptedException {
    Options.getCommandLine(args);

    File outputFile = new File(BaseRegionInstrumenter.DIRECTORY + "/" + this.getProgramName());
    Options.checkIfDeleteResult(outputFile);

    if (outputFile.exists()) {
      Collection<File> files = FileUtils.listFiles(outputFile, new String[]{"json"}, false);

      if (files.size() != 1) {
        throw new RuntimeException(
            "We expected to find 1 file in the directory, but that is not the case "
                + outputFile);
      }

      this.regionsToData = this.readFromFile(files.iterator().next());

      return;
    }

    if (Options.checkIfDeleteResult()) {
      this.compile();
    }

    if (Options.checkIfSave()) {
      this.instrument();
      this.writeToFile(this.regionsToData);
    }
  }

  protected abstract void writeToFile(Map<JavaRegion,T> regionsToData) throws IOException;

  protected abstract Map<JavaRegion,T> readFromFile(File next) throws IOException;

  public Map<JavaRegion, T> getRegionsToData() {
    return this.regionsToData;
  }
}
