package edu.cmu.cs.mvelezce.adapter.adapters.gpl;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Created by miguelvelez on 4/30/17. */
public abstract class AbstractGPLAdapter extends BaseAdapter {

  private static final String[] CONFIGURATIONS = {
    //            "BASE",
    //            "DIRECTED",
    //            "UNDIRECTED",
    "WEIGHTED",
    //            "SEARCH",
    //            "BFS",
    //            "DFS",
    //            "NUMBER",
    //            "CONNECTED",
    //            "STRONGLYCONNECTED",
    //            "TRANSPOSE",
    //            "CYCLE",
    //            "MSTPRIM",
    //            "MSTKRUSKAL",
    "SHORTEST"
  };

  public AbstractGPLAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static String[] adaptConfigurationToProgram(Set<String> configuration) {
    String[] sleepConfiguration = new String[CONFIGURATIONS.length];

    for (int i = 0; i < sleepConfiguration.length; i++) {
      if (configuration.contains(AbstractGPLAdapter.CONFIGURATIONS[i])) {
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
        performanceConfiguration.add(AbstractGPLAdapter.CONFIGURATIONS[i]);
      }
    }

    return performanceConfiguration;
  }

  //  @Override
  //  public void execute(Set<String> configuration, int iteration) {}
  //
  //  @Override
  //  public void execute(Set<String> configuration) {
  //    String[] argsArray = AbstractGPLAdapter.adaptConfigurationToProgram(configuration);
  //    StringBuilder args = new StringBuilder();
  //
  //    for (String arg : argsArray) {
  //      args.append(arg);
  //      args.append(" ");
  //    }
  //
  //    //        BaseAdapter.executeJavaProgram(programName, GPLMain.GPL_MAIN, this.mainClass,
  //    // this.directory, args.toString().trim());
  //  }
}
