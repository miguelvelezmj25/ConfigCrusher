package edu.cmu.cs.mvelezce.adapters;

/** Created by mvelezce on 4/26/17. */
// TODO do we need this class?
public abstract class AbstractSleepDynamicAdapter extends DynamicAdapter {

  //  private static final String[] CONFIGURATIONS = {"A", "B", "C", "D"};
  //
  //  private String mainClassFile;
  //
  //  public AbstractSleepDynamicAdapter(String mainClassFile) {
  //    this.mainClassFile = mainClassFile;
  //  }
  //
  //  //  // TODO pass the main class to execute
  //  //  public void execute(Set<String> configuration)
  //  //      throws ClassNotFoundException, NoSuchMethodException {
  //  //    Class<?> mainClass = this.loadClass(this.mainClassFile);
  //  //    Method method = mainClass.getMethod(DynamicAdapter.MAIN, String[].class);
  //  //
  //  //    //        try {
  //  //    //            Region program = Regions.getProgram();
  //  //    //            Regions.addExecutingRegion(program);
  //  //    //
  //  //    //            program.startTime();
  //  //    //            method.invoke(null, (Object) this.adaptConfiguration(configuration));
  //  //    //            program.endTime();
  //  //    //
  //  //    //            Regions.removeExecutingRegion(program);
  //  //    //        }
  //  //    //        catch (IllegalAccessException | InvocationTargetException e) {
  //  //    //            e.printStackTrace();
  //  //    //        }
  //  //  }
  //
  //  public String[] adaptConfiguration(Set<String> configuration) {
  //    String[] sleepConfiguration = new String[4];
  //
  //    for (int i = 0; i < sleepConfiguration.length; i++) {
  //      if (configuration.contains(AbstractSleepDynamicAdapter.CONFIGURATIONS[i])) {
  //        sleepConfiguration[i] = "true";
  //      } else {
  //        sleepConfiguration[i] = "false";
  //      }
  //    }
  //
  //    return sleepConfiguration;
  //  }
}
