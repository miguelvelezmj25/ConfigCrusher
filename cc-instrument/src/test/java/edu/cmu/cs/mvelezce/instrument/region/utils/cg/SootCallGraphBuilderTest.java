package edu.cmu.cs.mvelezce.instrument.region.utils.cg;

import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import org.junit.Assert;
import org.junit.Test;
import soot.jimple.toolkits.callgraph.CallGraph;

public class SootCallGraphBuilderTest {

  @Test
  public void trivial() {
    String entryPoint = BaseTrivialAdapter.MAIN_CLASS;
    String classDir = "../" + BaseTrivialAdapter.INSTRUMENTED_CLASS_PATH;

    CallGraph cg = SootCallGraphBuilder.buildCallGraph(entryPoint, classDir);
    Assert.assertNotNull(cg);
    Assert.assertTrue(cg.size() > 0);
  }
}
