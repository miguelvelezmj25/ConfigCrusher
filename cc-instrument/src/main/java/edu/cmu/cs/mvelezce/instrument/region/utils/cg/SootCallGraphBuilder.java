package edu.cmu.cs.mvelezce.instrument.region.utils.cg;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

public final class SootCallGraphBuilder {

  private SootCallGraphBuilder() {}

  public static CallGraph buildCallGraph(String entryPoint, String appPath) {
    initializeSoot(entryPoint, appPath);

    PackManager.v().getPack("wjpp").apply();
    PackManager.v().getPack("cg").apply();

    Scene.v().getOrMakeFastHierarchy();

    return Scene.v().getCallGraph();
  }

  private static void initializeSoot(String entryPoint, String appPath) {
    soot.G.reset();

    Options.v().set_no_bodies_for_excluded(true);
    Options.v().set_allow_phantom_refs(true);
    Options.v().set_output_format(Options.output_format_none);
    Options.v().set_soot_classpath(appPath);

    setSparkOptions();

    Options.v().set_whole_program(true);
    Options.v().setPhaseOption("cg", "trim-clinit:false");
    Options.v().setPhaseOption("jb.ulp", "off");
    Options.v().set_src_prec(Options.src_prec_java);

    // User options
    Options.v().setPhaseOption("jb", "use-original-names:true");

    // Options needed for instrumentation
    Options.v().set_keep_line_number(true);
    Options.v().set_keep_offset(true);
    Options.v().set_coffi(true);
    Options.v().set_ignore_classpath_errors(true);

    loadClassesAndBodies(entryPoint);
  }

  private static void loadClassesAndBodies(String entryPoint) {
    Scene.v().addBasicClass(entryPoint, SootClass.BODIES);
    Scene.v().loadNecessaryClasses();

    boolean hasClasses = false;

    SootClass c = Scene.v().forceResolve(entryPoint, SootClass.BODIES);
    if (c != null) {
      c.setApplicationClass();
      if (!c.isPhantomClass() && !c.isPhantom()) hasClasses = true;
    }

    if (!hasClasses) {
      throw new RuntimeException("Only phantom classes loaded, skipping analysis...");
    }
  }

  private static void setSparkOptions() {
    Options.v().setPhaseOption("cg.spark", "on");
    Options.v().setPhaseOption("cg.spark", "string-constants:true");
  }
}
