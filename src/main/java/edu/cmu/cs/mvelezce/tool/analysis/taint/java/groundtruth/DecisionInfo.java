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

public class DecisionInfo {

  private final Map<List<String>, Context> callingContextsToContexts = new HashMap<>();
  private final Map<List<String>, DecisionBranchCountTable> callingContextsToDecisionBranchTables = new HashMap<>();

  public Map<List<String>, Context> getCallingContextsToContexts() {
    return callingContextsToContexts;
  }

  public Map<List<String>, DecisionBranchCountTable> getCallingContextsToDecisionBranchTables() {
    return callingContextsToDecisionBranchTables;
  }

  public static Expression<String> toCNF(Context ctx, List<String> options) {
    Set<Set<String>> configs = ctx.getContext();
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

  void toCNF(Context ctx) {
    throw new UnsupportedOperationException(
        "First, change how the dynamic analysis stores executed configs");
//    Set<Set<String>> configs = ctx.getContext();
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

    if (!callingContextsToContexts.equals(that.callingContextsToContexts)) {
      return false;
    }
    return callingContextsToDecisionBranchTables.equals(that.callingContextsToDecisionBranchTables);
  }

  @Override
  public int hashCode() {
    int result = callingContextsToContexts.hashCode();
    result = 31 * result + callingContextsToDecisionBranchTables.hashCode();
    return result;
  }
}
