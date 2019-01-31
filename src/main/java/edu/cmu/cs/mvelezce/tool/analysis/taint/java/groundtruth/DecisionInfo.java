package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.bpodgursky.jbool_expressions.And;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Not;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.Variable;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Deprecated
public class DecisionInfo {

  private final Map<List<String>, VariabilityCtx> callingCtxsToVariabilityCtxs = new HashMap<>();
  private final Map<List<String>, DecisionBranchCountTable> callingCtxsToDecisionBranchTables = new HashMap<>();

  public Map<List<String>, VariabilityCtx> getCallingCtxsToVariabilityCtxs() {
    return callingCtxsToVariabilityCtxs;
  }

  public Map<List<String>, DecisionBranchCountTable> getCallingCtxsToDecisionBranchTables() {
    return callingCtxsToDecisionBranchTables;
  }

  public static Expression<String> toCNF(VariabilityCtx variabilityCtx, List<String> options) {
    Set<Set<String>> configs = variabilityCtx.getCtx();
    List<And<String>> ands = new ArrayList<>();

    for (Set<String> config : configs) {
      List<Expression<String>> vars = new ArrayList<>();

      for (String option : options) {
        Expression<String> var = Variable.of(option);

        if (!config.contains(option)) {
          var = Not.of(var);
        }

        vars.add(var);
      }

      And<String> and = And.of(vars);
      ands.add(and);
    }

    Expression<String> expr = Or.of(ands);
    expr = ExprParser.parse(expr.toString());
    return RuleSet.toCNF(expr);
  }

  void toCNF(VariabilityCtx variabilityCtx) {
    throw new UnsupportedOperationException(
        "First, change how the dynamic analysis stores executed configs");
//    Set<Set<String>> configs = ctx.getCtx();
//    List<And<String>> ands = new ArrayList<>();
//
//    for (Set<String> config : configs) {
//      List<Variable<String>> vars = new ArrayList<>();
//
//      if (config.isEmpty()) {
//        continue;
//      }
//
//      for (String string : config) {
//        Variable<String> var = Variable.of(string);
//        vars.add(var);
//      }
//
//      And<String> and = And.of(vars);
//      ands.add(and);
//    }
//
//    Expression<String> expr = Or.of(ands);
//    Expression<String> cnf = RuleSet.toCNF(expr);
//
//    System.out.println(cnf);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DecisionInfo that = (DecisionInfo) o;

    if (!callingCtxsToVariabilityCtxs.equals(that.callingCtxsToVariabilityCtxs)) {
      return false;
    }
    return callingCtxsToDecisionBranchTables.equals(that.callingCtxsToDecisionBranchTables);
  }

  @Override
  public int hashCode() {
    int result = callingCtxsToVariabilityCtxs.hashCode();
    result = 31 * result + callingCtxsToDecisionBranchTables.hashCode();
    return result;
  }
}
