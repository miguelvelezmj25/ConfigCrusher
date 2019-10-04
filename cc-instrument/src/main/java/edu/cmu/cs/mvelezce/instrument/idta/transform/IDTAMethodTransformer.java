package edu.cmu.cs.mvelezce.instrument.idta.transform;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import edu.cmu.cs.mvelezce.instrumenter.transform.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.instrumenter.transform.methodnode.BaseMethodTransformer;
import edu.cmu.cs.mvelezce.utils.Options;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IDTAMethodTransformer extends BaseMethodTransformer {

  private final Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints;

  private IDTAMethodTransformer(Builder builder)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException,
          InvocationTargetException {
    super(
        builder.programName,
        new DefaultClassTransformer(builder.classDir),
        builder.mainClass,
        builder.debug);

    this.regionsToConstraints = builder.regionsToConstraints;
  }

  @Override
  protected String getDebugDir() {
    return Options.DIRECTORY + "/instrument/idta/java/programs";
  }

  @Override
  public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
    Set<MethodNode> methodsToInstrument = new HashSet<>();
    Set<JavaRegion> regions = this.regionsToConstraints.keySet();

    if (InstrumenterUtils.getRegionsInClass(classNode, regions).isEmpty()) {
      return methodsToInstrument;
    }

    for (MethodNode methodNode : classNode.methods) {
      if (!InstrumenterUtils.getRegionsInMethod(methodNode, classNode, regions).isEmpty()) {
        methodsToInstrument.add(methodNode);
      }
    }

    return methodsToInstrument;
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    methodNode.visitMaxs(200, 200);
  }

  public static class Builder {

    private final String programName;
    private final String mainClass;
    private final String classDir;
    private final Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints;

    private boolean debug = false;

    public Builder(
        String programName,
        String mainClass,
        String classDir,
        Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints) {
      this.programName = programName;
      this.mainClass = mainClass;
      this.classDir = classDir;
      this.regionsToConstraints = regionsToConstraints;
    }

    public Builder setDebug(boolean debug) {
      this.debug = debug;
      return this;
    }

    public IDTAMethodTransformer build()
        throws InvocationTargetException, NoSuchMethodException, MalformedURLException,
            IllegalAccessException {
      return new IDTAMethodTransformer(this);
    }
  }
}
