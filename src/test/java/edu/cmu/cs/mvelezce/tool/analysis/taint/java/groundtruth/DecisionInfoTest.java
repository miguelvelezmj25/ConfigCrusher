package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import org.junit.Test;

@Deprecated
public class DecisionInfoTest {

  @Test
  public void toCNF0() {
//    Expression<String> e = ExprParser.parse(
//        "((A & B & C & D & E) | (A & B & C & D & !E) | (A & B & C & E & !D) | (A & B & C & !D & !E) | (A & B & D & E & !C) | (A & B & D & !C & !E) | (A & B & E & !C & !D) | (A & B & !C & !D & !E) | (A & C & D & E & !B) | (A & C & D & !B & !E) | (A & C & E & !B & !D) | (A & C & !B & !D & !E) | (A & D & E & !B & !C) | (A & D & !B & !C & !E) | (A & E & !B & !C & !D) | (A & !B & !C & !D & !E) | (B & C & D & E & !A) | (B & C & D & !A & !E) | (B & C & E & !A & !D) | (B & C & !A & !D & !E) | (B & D & E & !A & !C) | (B & D & !A & !C & !E) | (B & E & !A & !C & !D) | (B & !A & !C & !D & !E) | (C & D & E & !A & !B) | (C & D & !A & !B & !E) | (C & E & !A & !B & !D) | (C & !A & !B & !D & !E) | (D & E & !A & !B & !C) | (D & !A & !B & !C & !E) | (E & !A & !B & !C & !D) | (!A & !B & !C & !D & !E))");
//    Expression<String> expr = RuleSet.toCNF(e);
//    System.out.println(expr);
////    Expression<String> expected = Not.of(Variable.of("B"));
////
////    Context ctx = new Context();
////
////    Set<String> config = new HashSet<>();
////    ctx.addConfig(config);
////
////    config = new HashSet<>();
////    config.add("A");
////    ctx.addConfig(config);
////
////    List<String> options = new ArrayList<>();
////    options.add("A");
////    options.add("B");
////
////    Expression<String> cnf = DecisionInfo.toCNF(ctx, options);
////
////    Assert.assertEquals(expected, cnf);
  }

  @Test
  public void toCNF1() {
    Expression<String> e = ExprParser
        .parse("(A & C & !B) | (B & C & !A) | (A & B & C) | (C & !B) | (B & C) | (C & !A & !B)");
    Expression<String> expr = RuleSet.toCNF(e);
    System.out.println(expr);
  }

}