package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Not;
import com.bpodgursky.jbool_expressions.Variable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class DecisionInfoTest {

  @Test
  public void toCNF() {
    Expression<String> expected = Not.of(Variable.of("B"));

    Context ctx = new Context();

    Set<String> config = new HashSet<>();
    ctx.addConfig(config);

    config = new HashSet<>();
    config.add("A");
    ctx.addConfig(config);

    List<String> options = new ArrayList<>();
    options.add("A");
    options.add("B");

    Expression<String> cnf = DecisionInfo.toCNF(ctx, options);

    Assert.assertEquals(expected, cnf);
  }
}