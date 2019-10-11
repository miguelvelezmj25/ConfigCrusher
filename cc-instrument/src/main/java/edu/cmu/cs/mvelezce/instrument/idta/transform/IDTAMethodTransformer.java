package edu.cmu.cs.mvelezce.instrument.idta.transform;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.transformer.RegionTransformer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.instructionRegionMatcher.dynamic.DynamicInstructionRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.down.BaseDownIntraExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.down.idta.IDTADownIntraExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.up.BaseUpIntraExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.up.idta.IDTAUpIntraExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.startEndBlocksSetter.BaseStartEndRegionBlocksSetter;
import edu.cmu.cs.mvelezce.instrument.region.utils.startEndBlocksSetter.idta.IDTAStartEndRegionBlocksSetter;
import edu.cmu.cs.mvelezce.instrumenter.transform.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.utils.Options;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;

public class IDTAMethodTransformer extends RegionTransformer<Set<FeatureExpr>> {

  private static final String DEBUG_DIR = Options.DIRECTORY + "/instrument/idta/java/programs";
  private final BaseUpIntraExpander<Set<FeatureExpr>> upIntraExpander;
  private final BaseDownIntraExpander<Set<FeatureExpr>> downIntraExpander;
  private final BaseStartEndRegionBlocksSetter<Set<FeatureExpr>> startEndRegionBlocksSetter;

  private IDTAMethodTransformer(Builder builder)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException,
          InvocationTargetException {
    super(
        builder.programName,
        new DefaultClassTransformer(builder.classDir),
        builder.mainClass,
        builder.debug,
        builder.regionsToConstraints,
        new DynamicInstructionRegionMatcher());

    BaseIDTAExpander baseIDTAExpander = BaseIDTAExpander.getInstance();
    baseIDTAExpander.init(this.getRegionsToData().values());

    this.upIntraExpander =
        new IDTAUpIntraExpander(
            builder.programName,
            DEBUG_DIR,
            builder.options,
            this.getBlockRegionMatcher(),
            this.getRegionsToData(),
            baseIDTAExpander);
    this.downIntraExpander =
        new IDTADownIntraExpander(
            builder.programName,
            DEBUG_DIR,
            builder.options,
            this.getBlockRegionMatcher(),
            this.getRegionsToData(),
            baseIDTAExpander);
    this.startEndRegionBlocksSetter =
        new IDTAStartEndRegionBlocksSetter(
            builder.programName,
            DEBUG_DIR,
            builder.options,
            this.getBlockRegionMatcher(),
            this.getRegionsToData(),
            baseIDTAExpander);
  }

  @Override
  protected String getDebugDir() {
    return DEBUG_DIR;
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    System.err.println("Implement actually instrumenting the method");
    methodNode.visitMaxs(200, 200);
  }

  @Override
  protected void transformRegions(Set<ClassNode> classNodes) {
    this.propagateRegionsIntra(classNodes);
    System.err.println("Expand regions interprocedural and repeat until fix point");
    this.setStartAndEndBlocks(classNodes);
  }

  private void setStartAndEndBlocks(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToProcess = this.getMethodsToInstrument(classNode);

      if (methodsToProcess.isEmpty()) {
        continue;
      }

      for (MethodNode methodNode : methodsToProcess) {
        this.startEndRegionBlocksSetter.processBlocks(methodNode, classNode);

        if (this.debug()) {
          this.startEndRegionBlocksSetter.debugBlockData(methodNode, classNode);
        }
      }
    }
  }

  private void propagateRegionsIntra(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToProcess = this.getMethodsToInstrument(classNode);

      if (methodsToProcess.isEmpty()) {
        continue;
      }

      for (MethodNode methodNode : methodsToProcess) {
        this.expandRegionsIntra(methodNode, classNode);
      }
    }
  }

  private void expandRegionsIntra(MethodNode methodNode, ClassNode classNode) {
    System.err.println(
        "Might have to use equivalence or implication instead of equals when determining if we can expand regions");
    boolean updatedBlocks = true;

    while (updatedBlocks) {
      updatedBlocks = this.upIntraExpander.processBlocks(methodNode, classNode);
      updatedBlocks = updatedBlocks || this.downIntraExpander.processBlocks(methodNode, classNode);
    }

    if (this.debug()) {
      this.upIntraExpander.debugBlockData(methodNode, classNode);
      this.upIntraExpander.validateAllBlocksHaveRegions(methodNode, classNode);
    }
  }

  public static class Builder {

    private final String programName;
    private final String mainClass;
    private final String classDir;
    private final Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints;
    private final Set<String> options;

    private boolean debug = false;

    public Builder(
        String programName,
        String mainClass,
        String classDir,
        Set<String> options,
        Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints) {
      this.programName = programName;
      this.mainClass = mainClass;
      this.classDir = classDir;
      this.regionsToConstraints = regionsToConstraints;
      this.options = options;
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
