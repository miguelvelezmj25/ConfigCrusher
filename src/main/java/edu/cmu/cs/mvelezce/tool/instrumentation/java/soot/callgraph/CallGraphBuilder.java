package edu.cmu.cs.mvelezce.tool.instrumentation.java.soot.callgraph;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.soot.config.UserSootConfig;
import java.io.File;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

public class CallGraphBuilder {

  private static final String LIB_PATH = "/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/jce.jar";

  private CallGraphBuilder() {
  }

  public static CallGraph buildCallGraph(String entryPoint, String pathToClasses) {
    initializeSoot(entryPoint, pathToClasses);

    PackManager.v().getPack("wjpp").apply();
    PackManager.v().getPack("cg").apply();

    return Scene.v().getCallGraph();
  }

  private static void initializeSoot(String entryPoint, String pathToClasses) {
    soot.G.reset();

    Options.v().set_no_bodies_for_excluded(true);
    Options.v().set_allow_phantom_refs(true);
    Options.v().set_output_format(Options.output_format_none);
    Options.v().set_whole_program(true);

    Options.v().set_soot_classpath(appendClasspath(pathToClasses));

    setSparkOptions();

    // Specify additional options required for the callgraph
    Options.v().setPhaseOption("cg", "trim-clinit:false");

    // do not merge variables (causes problems with PointsToSets)
    Options.v().setPhaseOption("jb.ulp", "off");

    Options.v().set_src_prec(Options.src_prec_java);

    //at the end of setting: load user settings:
    UserSootConfig.setUserSootOptions(Options.v());

    loadClassesAndBodies(entryPoint);
  }

  private static void loadClassesAndBodies(String entryPoint) {
    // load all entryPoint classes with their bodies
    Scene.v().addBasicClass(entryPoint, SootClass.BODIES);
    Scene.v().loadNecessaryClasses();

    boolean hasClasses = false;

    SootClass c = Scene.v().forceResolve(entryPoint, SootClass.BODIES);
    if (c != null) {
      c.setApplicationClass();
      if (!c.isPhantomClass() && !c.isPhantom()) {
        hasClasses = true;
      }
    }

    if (!hasClasses) {
      throw new RuntimeException("Only phantom classes loaded, skipping analysis...");
    }
  }

  private static void setSparkOptions() {
    Options.v().setPhaseOption("cg.spark", "on");
    Options.v().setPhaseOption("cg.spark", "string-constants:true");
  }

  private static String appendClasspath(String appPath) {
    String s = (appPath != null && !appPath.isEmpty()) ? appPath : "";

    if (!s.isEmpty()) {
      s += File.pathSeparator;
    }
    s += LIB_PATH;

    return s;
  }

}
