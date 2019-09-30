package edu.cmu.cs.mvelezce.adapter.adapters.zipme;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.HashSet;
import java.util.Set;

/** Created by miguelvelez on 4/30/17. */
public abstract class AbstractZipmeAdapter extends BaseAdapter {

  private static final String[] CONFIGURATIONS = {
    "FEATURECRC",
    "FEATUREArchiveCheck",
    "FEATUREAdlerThreeTwoChecksum",
    "FEATUREDerivativeGZIPCRC",
    "FEATUREDerivativeCompressCRC",
    "FEATUREDerivativeExtractCRC",
    "FEATUREDerivativeCompressGZIP",
    "FEATUREDerivativeCompressAdlerThreeTwoChecksum",
    "FEATUREDerivativeCompressGZIPCRC"
  };

  public AbstractZipmeAdapter(String programName, String mainClass, String directory) {
    //        super(programName, mainClass, directory);
    super(null, null, null, null);
  }

  public static String[] adaptConfigurationToProgram(Set<String> configuration) {
    String[] sleepConfiguration = new String[AbstractZipmeAdapter.CONFIGURATIONS.length];

    for (int i = 0; i < sleepConfiguration.length; i++) {
      if (configuration.contains(AbstractZipmeAdapter.CONFIGURATIONS[i])) {
        sleepConfiguration[i] = "true";
      } else {
        sleepConfiguration[i] = "false";
      }
    }

    return sleepConfiguration;
  }

  public static Set<String> adaptConfigurationToPerformanceMeasurement(String[] configuration) {
    Set<String> performanceConfiguration = new HashSet<>();

    for (int i = 0; i < configuration.length; i++) {
      if (configuration[i].equals("true")) {
        performanceConfiguration.add(AbstractZipmeAdapter.CONFIGURATIONS[i]);
      }
    }

    return performanceConfiguration;
  }

  @Override
  public void execute(Set<String> configuration, int iteration) {}

  @Override
  public void execute(Set<String> configuration) {
    String[] argsArray = AbstractZipmeAdapter.adaptConfigurationToProgram(configuration);
    StringBuilder args = new StringBuilder();

    for (String arg : argsArray) {
      args.append(arg);
      args.append(" ");
    }

    //        BaseAdapter.executeJavaProgram(programName, ZipmeMain.ZIPME_MAIN, this.mainClass,
    // this.directory, args.toString().trim());
  }
}
