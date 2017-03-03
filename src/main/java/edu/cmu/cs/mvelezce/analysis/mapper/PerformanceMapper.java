package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.analysis.interpreter.Interpreter;
import edu.cmu.cs.mvelezce.analysis.taint.TaintAnalysis;
import edu.cmu.cs.mvelezce.analysis.visitor.VisitorReplacer;
import edu.cmu.cs.mvelezce.analysis.visitor.VisitorReturner;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementIf;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementSleep;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementTimed;
import edu.cmu.cs.mvelezce.language.lexer.Lexer;
import edu.cmu.cs.mvelezce.language.parser.Parser;

import java.util.*;

/**
 * TODO
 * Created by miguelvelez on 2/11/17.
 */
public class PerformanceMapper {

    private static List<Class<? extends Statement>> relevantStatementsClasses = new ArrayList<Class<? extends Statement>>() {
        {
            add(StatementSleep.class);
            add(StatementIf.class);
        }
    };

    public static Map<Set<String>, Integer> calculatePerformance(String program, Set<ExpressionConfigurationConstant> parameters) {
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);

        Map<Statement, Set<ExpressionConfigurationConstant>> relevantStatementsToOptions =
                PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
        ast = PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, relevantStatementsToOptions.keySet());
        Set<Set<ExpressionConfigurationConstant>> relevantOptions = new HashSet<>(relevantStatementsToOptions.values());
        Set<Set<String>> configurationsToExecute = PerformanceMapper.getConfigurationsToExecute(relevantOptions);

        Set<PerformanceEntry> measuredPerformance = PerformanceMapper.measurePerformance(ast, configurationsToExecute);
        Map<Set<String>, Integer> precitedAllPerformance = null;
//                PerformanceMapper.predictPerformanceForAllConfigurations(parameters, relevantOptions, measuredPerformance);

        return precitedAllPerformance;
    }

    public static Set<PerformanceEntry> measurePerformance(Statement ast, Set<Set<String>> configurationsToExecute) {
        Interpreter interpreter = new Interpreter(ast);

        Set<PerformanceEntry> configurationsToPerformance = new HashSet<>();

        for (Set<String> configuration : configurationsToExecute) {
            interpreter.evaluate(configuration);
            configurationsToPerformance.add(new PerformanceEntry(configuration, interpreter.getTimedBlocks(), interpreter.getTotalExecutionTime()));

            // TODO calculate the performance of other configurations and see, in the future if we can reduce the number of configurations we need to execute
        }

        return configurationsToPerformance;
    }

//    public static Map<Set<String>, Integer> predictPerformanceForAllConfigurations(
//            Set<ExpressionConfigurationConstant> parameters, Set<Set<ExpressionConfigurationConstant>> relevantOptions,
//            Set<PerformanceEntry> measuredPerformance) {
//        Set<Set<String>> powerSet = Helper.getConfigurations(parameters);
//        Map<Set<String>, Integer> performanceMap = new HashMap<>();
//
//        for(Set<String> configuration : powerSet) {
//            boolean calculatedConfiguration = false;
//            for(PerformanceEntry entry : measuredPerformance) {
//                if (entry.getConfiguration().equals(configuration)) {
//                    performanceMap.put(configuration, entry.getTotalTime());
//                    calculatedConfiguration = true;
//                    break;
//                }
//            }
//
//            if(calculatedConfiguration) { continue; }
//
//            System.out.println("Have to calculate: " + configuration);
//            Map<Statement, Integer> predictedBlockToTime = new HashMap<>();
//
//            int totalTime = 0;
//            // Predicting each relevant block at a time
//            for(Set<ExpressionConfigurationConstant> entry : relevantStatementsToOptions.entrySet()) {
//                Set<ExpressionConfigurationConstant> affectingOptions = entry.getValue();
//                Set<String> affectingOptionsConvenient = new HashSet<>();
//
//                // Compare with actual string values
//                for(ExpressionConfigurationConstant option : affectingOptions) {
//                    affectingOptionsConvenient.add(option.getName());
//                }
//
//                for(PerformanceEntry performanceEntry : configurationsToPerformance) {
//                    Set<String> valuesOfAffectingOptionsInConfiguration = new HashSet<>(affectingOptionsConvenient);
//                    valuesOfAffectingOptionsInConfiguration.retainAll(configuration);
//
//                    Set<String> valuesOfAffectionOptionsInMeasuredConfiguration = new HashSet<>(affectingOptionsConvenient);
//                    valuesOfAffectionOptionsInMeasuredConfiguration.retainAll(performanceEntry.getConfiguration());
//
//                    // If the current measured configuration has the same configuration in this block
//                    if(valuesOfAffectionOptionsInMeasuredConfiguration.equals(valuesOfAffectingOptionsInConfiguration)) {
//                        if(entry.getKey() instanceof StatementIf) {
//                            StatementIf statementIf = (StatementIf) entry.getKey();
//                            Integer time = performanceEntry.getBlockToTime().get(statementIf.getThenBlock());
//
//                            if(time != null) {
//                                predictedBlockToTime.put(statementIf, time);
//                            }
//                        }
//                        else if(entry.getKey() instanceof StatementSleep) {
//                            // TODO sleep statement
//                        }
//
//                        totalTime = performanceEntry.getBaseTime();
//                        break;
//                    }
//                }
//            }
//
//            for(Map.Entry<Statement, Integer> entry : predictedBlockToTime.entrySet()) {
//                totalTime += entry.getValue();
//            }
//
//            performanceMap.put(configuration, totalTime);
//        }
//
//        return performanceMap;
//    }

    public static Map<Statement, Set<ExpressionConfigurationConstant>> getRelevantStatementsToOptions(Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted) {
        Map<Statement, Set<ExpressionConfigurationConstant>> relevantStatementToOptions = new HashMap<>();

        for(Map.Entry<BasicBlock, Set<TaintAnalysis.PossibleTaint>> entry : instructionsToTainted.entrySet()) {
            if(PerformanceMapper.relevantStatementsClasses.contains(entry.getKey().getStatement().getClass())) {
                RelevantInfoGetterVisitor performanceStatementVisitor = new RelevantInfoGetterVisitor(entry.getValue());
                Set<ExpressionConfigurationConstant> result = performanceStatementVisitor.getRelevantInfo(entry.getKey().getStatement());

                if(!result.isEmpty()) {
                    relevantStatementToOptions.put(entry.getKey().getStatement(), result);
                }
            }
        }

        return relevantStatementToOptions;
    }

    public static Set<Set<String>> getConfigurationsToExecute(Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet) {
        Set<Set<ExpressionConfigurationConstant>> relevantUniqueOptions = new HashSet<>();

        // Calculates which options are subsets of other options
        for(Set<ExpressionConfigurationConstant> relevantOptions : relevantOptionsSet) {
            if(relevantUniqueOptions.isEmpty()) {
                relevantUniqueOptions.add(relevantOptions);
                continue;
            }

            Set<Set<ExpressionConfigurationConstant>> toRemove = new HashSet<>();
            Set<Set<ExpressionConfigurationConstant>> toAdd = new HashSet<>();

            for(Set<ExpressionConfigurationConstant> options : relevantUniqueOptions) {
                if(options.equals(relevantOptions) || options.containsAll(relevantOptions)) {
                    toAdd.remove(relevantOptions);
                    break;
                }

                if(!options.containsAll(relevantOptions) && relevantOptions.containsAll(options)) {
                    toRemove.add(options);
                }

                toAdd.add(relevantOptions);

            }

            relevantUniqueOptions.removeAll(toRemove);
            relevantUniqueOptions.addAll(toAdd);
        }

        // Get the configurations for each option
        Map<Set<ExpressionConfigurationConstant>, Set<Set<String>>> optionsToConfigurationsToExecute = new HashMap<>();

        for(Set<ExpressionConfigurationConstant> option : relevantUniqueOptions) {
            Set<Set<String>> configurationsToExecuteForOption = new HashSet<>();
            configurationsToExecuteForOption.addAll(Helper.getConfigurations(option));
            optionsToConfigurationsToExecute.put(option, configurationsToExecuteForOption);
        }

        // Compresses which configurations to execute
        Set<Set<String>> configurationsToExecute = new HashSet<>();

        if(optionsToConfigurationsToExecute.isEmpty()) {
            return configurationsToExecute;
        }

        if(optionsToConfigurationsToExecute.size() == 1) {
            configurationsToExecute.addAll(optionsToConfigurationsToExecute.entrySet().iterator().next().getValue());
            return configurationsToExecute;
        }

        Iterator<Map.Entry<Set<ExpressionConfigurationConstant>, Set<Set<String>>>> optionsToConfigurationsToExecuteIterator = optionsToConfigurationsToExecute.entrySet().iterator();
        Map.Entry<Set<ExpressionConfigurationConstant>, Set<Set<String>>> entry1 = optionsToConfigurationsToExecuteIterator.next();

        while(optionsToConfigurationsToExecuteIterator.hasNext()) {
            Map.Entry<Set<ExpressionConfigurationConstant>, Set<Set<String>>> entry2 = optionsToConfigurationsToExecuteIterator.next();

            Set<ExpressionConfigurationConstant> pivotOptions = new HashSet<>(entry1.getKey());
            pivotOptions.retainAll(entry2.getKey());

            Set<String> pivotOptionsConvenient = new HashSet<>();

            for(ExpressionConfigurationConstant expressionConfigurationConstant : pivotOptions) {
                pivotOptionsConvenient.add(expressionConfigurationConstant.getName());
            }

            configurationsToExecute = new HashSet<>();
            Iterator<Set<String>> set1 = entry1.getValue().iterator();
            Iterator<Set<String>> set2 = entry2.getValue().iterator();

            while (set1.hasNext() && set2.hasNext()) {
                Set<String> configurationInSet1 = set1.next();

                Set<String> valuePivotOptionsInSet1 = new HashSet<>(configurationInSet1);
                valuePivotOptionsInSet1.retainAll(pivotOptionsConvenient);
                boolean merged = false;

                while(set2.hasNext()) {
                    Set<String> configurationInSet2 = set2.next();

                    Set<String> valuePivotOptionsInSet2 = new HashSet<>(configurationInSet2);
                    valuePivotOptionsInSet2.retainAll(pivotOptionsConvenient);

                    if(valuePivotOptionsInSet1.equals(valuePivotOptionsInSet2)) {
                        Set<String> compressedConfiguration = new HashSet<>(configurationInSet1);
                        compressedConfiguration.addAll(configurationInSet2);
                        configurationsToExecute.add(compressedConfiguration);

                        entry2.getValue().remove(configurationInSet2);
                        merged = true;
                        break;
                    }
                }

                if(!merged) {
                    throw new IllegalArgumentException("Bye");
                }

                set2 = entry2.getValue().iterator();
            }

            while (set1.hasNext()) {
                configurationsToExecute.add(set1.next());
            }

            while (set2.hasNext()) {
                configurationsToExecute.add(set2.next());
            }

//            System.out.println(configurationsToExecute);
            set1 = configurationsToExecute.iterator();
        }

        return configurationsToExecute;

    }

    public static Statement instrumentProgramToTimeRelevantStatements(Statement program, Set<Statement> relevantStatements) {
        AddTimedVisitor addTimedVisitor = new AddTimedVisitor(relevantStatements);
        return program.accept(addTimedVisitor);
    }


//    /**
//     * TODO
//     * @param considerParameters
//     */
//    protected void pruneAndMapConfigurations(Set<String> considerParameters, Set<String> lastConfiguration) {
//        List<Set<String>> redundantConfigurations = new ArrayList<>();
//
//        if(!considerParameters.isEmpty()) { // TODO check if this is needed
//            for (Set<String> configuration : this.allConfigurations) {
//                Set<String> possibleEquivalentConfiguration = new HashSet<>(configuration);
//                possibleEquivalentConfiguration.retainAll(considerParameters);
//
//                if(possibleEquivalentConfiguration.equals(lastConfiguration)) {
//                    redundantConfigurations.add(configuration);
//                }
//            }
//
//            if (!redundantConfigurations.isEmpty()) {
//                for (Set<String> redundantConfiguration : redundantConfigurations) {
//                    this.allConfigurations.remove(redundantConfiguration);
//                }
//            }
//        }
//    }

//    mapConfigurationToPerformanceAfterPruning //TODO

    private static class RelevantInfoGetterVisitor extends VisitorReturner {
        private Set<TaintAnalysis.PossibleTaint> taintedVariables;
        private Set<ExpressionConfigurationConstant> relevantOptions;

        public RelevantInfoGetterVisitor(Set<TaintAnalysis.PossibleTaint> taintedVariables) {
            this.taintedVariables = taintedVariables;
            this.relevantOptions = new HashSet<>();
        }

        public Set<ExpressionConfigurationConstant> getRelevantInfo(Statement statement) {
            statement.accept(this);

            return this.relevantOptions;
        }

        @Override
        public Expression visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant) {
            this.relevantOptions.add(expressionConfigurationConstant);

            return expressionConfigurationConstant;
        }

        @Override
        public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
                for(TaintAnalysis.PossibleTaint taintedVariable : this.taintedVariables) {
                    if(taintedVariable.getVariable().equals(expressionVariable)) {
                        for(ExpressionConfigurationConstant parameter : taintedVariable.getConfigurations()) {
                            this.relevantOptions.add(parameter);
                        }
                    }
                }

            return expressionVariable;
        }

        @Override
        public Void visitStatementIf(StatementIf statementIf) {
            statementIf.getCondition().accept(this);

            return null;
        }

        @Override
        public Void visitStatementSleep(StatementSleep statementSleep) {
            statementSleep.getTime().accept(this);

            return null;
        }
    }

    /**
     * Concrete visitor that replaces statements with StatementTimed for measuring time
     */
    private static class AddTimedVisitor extends VisitorReplacer {
        private Set<Statement> relevantStatements;

        /**
         * Instantiate a {@code AddTimedVisitor}.
         *
         * @param relevantStatements
         */
        public AddTimedVisitor(Set<Statement> relevantStatements) {
            this.relevantStatements = relevantStatements;
        }

        /**
         * Replace the thenBlock of a StatementIf if the entire statement is relevant.
         *
         * @param statementIf
         * @return
         */
        @Override
        public Statement visitStatementIf(StatementIf statementIf) {
            if(this.relevantStatements.contains(statementIf)) {
                StatementTimed thenBlock = new StatementTimed(statementIf.getThenBlock());

                return new StatementIf(statementIf.getCondition(), thenBlock);
            }

            return super.visitStatementIf(statementIf);
        }

        /**
         * Replace the thenBlock of a StatementIf if the entire statement is relevant.
         *
         * @param statementSleep
         * @return
         */
        @Override
        public Statement visitStatementSleep(StatementSleep statementSleep) {
            if(relevantStatements.contains(statementSleep)) {
                return new StatementTimed(statementSleep);
            }

            return statementSleep;
        }

    }

    private static class PerformanceEntry {
        private Set<String> configuration;
        private Map<Statement, Integer> blockToTime;
        private int totalTime;
        private int baseTime;

        public PerformanceEntry(Set<String> configuration, Map<Statement, Integer> blockToTime, int totalTime) {
            this.configuration = configuration;
            this.blockToTime = blockToTime;
            this.totalTime = totalTime;
            this.baseTime = totalTime;

            for(Map.Entry<Statement, Integer> entry : this.blockToTime.entrySet()) {
                this.baseTime -= entry.getValue();
            }
        }

        public Set<String> getConfiguration() { return this.configuration; }

        public Map<Statement, Integer> getBlockToTime() { return this.blockToTime; }

        public int getTotalTime() { return this.totalTime; }

        public int getBaseTime() { return this.baseTime; }

        @Override
        public String toString() {
            return "PerformanceEntry{" +
                    "configuration=" + configuration +
                    ", blockToTime=" + blockToTime +
                    ", totalTime=" + totalTime +
                    '}';
        }
    }
}
