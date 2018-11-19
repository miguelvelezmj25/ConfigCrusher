package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.bpodgursky.jbool_expressions.And;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Not;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.Variable;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DynamicAnalysisSpecification {

  static Set<String> getMinimalSetOfOptions(DecisionBranchCountTable decisionTable) {
    Set<String> minimalOptionSet = new HashSet<>();
    Set<String> options = decisionTable.getOptions();
    Map<Map<String, Boolean>, ThenElseCounts> table = decisionTable.getTable();

    for (String option : options) {
      Set<Map<String, Boolean>> configsWithValuesWhereOptionIsFalse = DynamicAnalysisSpecification
          .getConfigsWithValuesWhereOptionIsFalse(option, table.keySet());

      // Takes care of the case where there are no executed configs with the option set to false
      for (Map<String, Boolean> configWithValuesWhereOptionIsFalse : configsWithValuesWhereOptionIsFalse) {
        Map<String, Boolean> configWithValuesWhereOptionIsTrue = new HashMap<>(
            configWithValuesWhereOptionIsFalse);
        configWithValuesWhereOptionIsTrue.put(option, true);

        ThenElseCounts thenElseCountsWithTrue = table.get(configWithValuesWhereOptionIsTrue);

        // Takes care of the case where there are no executed configs with the option set to true
        if (thenElseCountsWithTrue == null) {
          continue;
        }

        boolean equalBranchCounts = table.get(configWithValuesWhereOptionIsFalse)
            .equals(thenElseCountsWithTrue);

        if (!equalBranchCounts) {
          minimalOptionSet.add(option);
          break;
        }
      }
    }

    return minimalOptionSet;
  }

  private static Set<Map<String, Boolean>> getConfigsWithValuesWhereOptionIsFalse(String option,
      Set<Map<String, Boolean>> configsWithValues) {
    Set<Map<String, Boolean>> configsWithValuesWhereOptionIsFalse = new HashSet<>();

    for (Map<String, Boolean> entry : configsWithValues) {
      if (!entry.get(option)) {
        configsWithValuesWhereOptionIsFalse.add(entry);
      }
    }

    return configsWithValuesWhereOptionIsFalse;
  }


  public static void toCNF(Context ctx, List<String> options) {
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
    Expression<String> cnf = RuleSet.toCNF(expr);

    System.out.println(cnf);
  }

  public static void toCNF(Context ctx) {
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
}
