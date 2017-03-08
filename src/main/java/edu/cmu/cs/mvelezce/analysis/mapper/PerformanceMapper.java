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

    public static Map<Set<String>, Integer> calculatePerformance(String program,
                                                                 Set<ExpressionConfigurationConstant> parameters) {
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
        Set<PerformanceEntry> configurationsToPerformance = new HashSet<>();

        for (Set<String> configuration : configurationsToExecute) {
            Interpreter interpreter = new Interpreter(ast);
            interpreter.evaluate(configuration);
            configurationsToPerformance.add(new PerformanceEntry(configuration, interpreter.getTimedBlocks(),
                    interpreter.getTotalExecutionTime()));
            // TODO calculate the performance of other configurations and see, in the future if we can reduce the number of configurations we need to execute
        }

        return configurationsToPerformance;
    }

    public static Map<Statement, Set<ExpressionConfigurationConstant>> getRelevantStatementsToOptions(
            Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted) {
        Map<Statement, Set<ExpressionConfigurationConstant>> relevantStatementToOptions = new HashMap<>();

        for(Map.Entry<BasicBlock, Set<TaintAnalysis.PossibleTaint>> entry : instructionsToTainted.entrySet()) {
            if(PerformanceMapper.relevantStatementsClasses.contains(entry.getKey().getStatement().getClass())) {
                RelevantInfoGetterVisitor performanceStatementVisitor = new RelevantInfoGetterVisitor(entry.getValue());
                Set<ExpressionConfigurationConstant> possibleTaintingConfigurations =
                        performanceStatementVisitor.getRelevantInfo(entry.getKey().getStatement());

                if(!possibleTaintingConfigurations.isEmpty()) {
                    relevantStatementToOptions.put(entry.getKey().getStatement(), possibleTaintingConfigurations);
                }
            }
        }

        return relevantStatementToOptions;
    }

    public static Set<Set<ExpressionConfigurationConstant>> getUniqueRelevantOptions(
            Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet) {
        Set<Set<ExpressionConfigurationConstant>> uniqueRelevantOptions = new HashSet<>();

        for(Set<ExpressionConfigurationConstant> relevantOptions : relevantOptionsSet) {
            if(uniqueRelevantOptions.isEmpty()) {
                uniqueRelevantOptions.add(relevantOptions);
                continue;
            }

            Set<Set<ExpressionConfigurationConstant>> optionsToRemove = new HashSet<>();
            Set<Set<ExpressionConfigurationConstant>> optionsToAdd = new HashSet<>();

            for(Set<ExpressionConfigurationConstant> options : uniqueRelevantOptions) {
                if(options.equals(relevantOptions) || options.containsAll(relevantOptions)) {
                    optionsToAdd.remove(relevantOptions);
                    break;
                }

                if(!options.containsAll(relevantOptions) && relevantOptions.containsAll(options)) {
                    optionsToRemove.add(options);
                }

                optionsToAdd.add(relevantOptions);
            }

            uniqueRelevantOptions.removeAll(optionsToRemove);
            uniqueRelevantOptions.addAll(optionsToAdd);
        }

        return uniqueRelevantOptions;

    }

    public static Set<Set<String>> getConfigurationsToExecute(
            Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet) {
        // Calculates which options are subsets of other options
        Set<Set<ExpressionConfigurationConstant>> uniqueRelevantOptions =
                PerformanceMapper.getUniqueRelevantOptions(relevantOptionsSet);

        // Get the configurations for each option
        Map<Set<ExpressionConfigurationConstant>, Set<Set<String>>> optionsToConfigurationsToExecute = new HashMap<>();

        for(Set<ExpressionConfigurationConstant> relevantOptions : uniqueRelevantOptions) {
            Set<Set<String>> configurationsToExecuteForOption = new HashSet<>();
            configurationsToExecuteForOption.addAll(Helper.getConfigurations(relevantOptions));
            optionsToConfigurationsToExecute.put(relevantOptions, configurationsToExecuteForOption);
        }

        // Compresses which configurations to execute
        Set<Set<String>> configurationsToExecute = new HashSet<>();

        // Base case covering 0 and 1 configurations
        if(optionsToConfigurationsToExecute.size() == 1) {
            configurationsToExecute.addAll(optionsToConfigurationsToExecute.entrySet().iterator().next().getValue());
            return configurationsToExecute;
        }

        Iterator<Map.Entry<Set<ExpressionConfigurationConstant>, Set<Set<String>>>> optionsToConfigurationsToExecuteIterator =
                optionsToConfigurationsToExecute.entrySet().iterator();
        Map.Entry<Set<ExpressionConfigurationConstant>, Set<Set<String>>> entry1 =
                optionsToConfigurationsToExecuteIterator.next();
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsCovered = new HashSet<>();
        relevantOptionsCovered.add(entry1.getKey());

        while(optionsToConfigurationsToExecuteIterator.hasNext()) {
            Map.Entry<Set<ExpressionConfigurationConstant>, Set<Set<String>>> entry2 =
                    optionsToConfigurationsToExecuteIterator.next();
            relevantOptionsCovered.add(entry2.getKey());

            Set<ExpressionConfigurationConstant> pivotOptions = new HashSet<>(entry1.getKey());
            pivotOptions.retainAll(entry2.getKey());
            System.out.println("\nPivot: " + pivotOptions + " for set1: " + entry1.getKey() + " set2: " + entry2.getKey());

            Set<String> pivotOptionsConvenient = new HashSet<>();

            for(ExpressionConfigurationConstant pivotOption : pivotOptions) {
                pivotOptionsConvenient.add(pivotOption.getName());
            }

            configurationsToExecute = new HashSet<>();

            if(entry2.getKey().containsAll(entry1.getKey())) {
                throw new RuntimeException("I have never seen the case where the built sets " + entry1.getKey() + " is a " +
                        "subset of a previously established set " + entry2.getKey());
            }

            if(entry1.getKey().containsAll(entry2.getKey())) {
                PerformanceMapper.subsetMerging(entry1, relevantOptionsCovered, configurationsToExecute);
            }
            else {
                PerformanceMapper.simpleMerging(entry1, entry2, pivotOptionsConvenient, configurationsToExecute);
            }

            System.out.println(configurationsToExecute);

            Set<ExpressionConfigurationConstant> newCalculatedOptions = new HashSet<>(entry1.getKey());
            newCalculatedOptions.addAll(entry2.getKey());
            Map<Set<ExpressionConfigurationConstant>, Set<Set<String>>> entryHolder = new HashMap<>();
            entryHolder.put(newCalculatedOptions, configurationsToExecute);
            entry1 = entryHolder.entrySet().iterator().next();
        }

        return configurationsToExecute;
    }

    private static void subsetMerging(Map.Entry<Set<ExpressionConfigurationConstant>, Set<Set<String>>> superSet,
                                      Set<Set<ExpressionConfigurationConstant>> relevantOptionsCovered,
                                      Set<Set<String>> configurationsToExecute) {
        Set<Set<String>> powerSet = Helper.getConfigurations(superSet.getKey());

        // Getting the power set for all options covered
        Map<Set<String>, Set<Set<String>>> relevantOptionsCoveredToPowerSet = new HashMap<>();

        for(Set<ExpressionConfigurationConstant> relevantOptions : relevantOptionsCovered) {
            Set<String> relevantOptionsConvenient = new HashSet<>();

            for(ExpressionConfigurationConstant relevantOption : relevantOptions) {
                relevantOptionsConvenient.add(relevantOption.getName());
            }

            relevantOptionsCoveredToPowerSet.put(relevantOptionsConvenient, Helper.getConfigurations(relevantOptions));
        }

        // Go through the powerset of the superset checking if a possible configuration satisfies constrains in the
        // smaller powersets
        for(Set<String> possibleConfiguration : powerSet) {
            System.out.println(possibleConfiguration);
            boolean addConfiguration = true;

            for(Map.Entry<Set<String>, Set<Set<String>>> entry : relevantOptionsCoveredToPowerSet.entrySet()) {
                Set<String> valueOfPossibleConfigurationInCurrentOptions = new HashSet<>(entry.getKey());
                valueOfPossibleConfigurationInCurrentOptions.retainAll(possibleConfiguration);

                if(!entry.getValue().contains(valueOfPossibleConfigurationInCurrentOptions)) {
                    addConfiguration = false;
                    break;
                }
            }

            if(addConfiguration) {
                System.out.println("Configuration satisfied all");
                configurationsToExecute.add(possibleConfiguration);

                for(Map.Entry<Set<String>, Set<Set<String>>> entry : relevantOptionsCoveredToPowerSet.entrySet()) {
                    Set<String> valueOfPossibleConfigurationInCurrentOptions = new HashSet<>(entry.getKey());
                    valueOfPossibleConfigurationInCurrentOptions.retainAll(possibleConfiguration);
                    entry.getValue().remove(valueOfPossibleConfigurationInCurrentOptions);
                }

            }
        }

        // Sanity check
        for(Map.Entry<Set<String>, Set<Set<String>>> entry : relevantOptionsCoveredToPowerSet.entrySet()) {
            if(!entry.getValue().isEmpty()) {
                throw new RuntimeException("Did not satisfy all constrains from previous options");
            }
        }
    }

    private static void simpleMerging(Map.Entry<Set<ExpressionConfigurationConstant>, Set<Set<String>>> entry1,
                                     Map.Entry<Set<ExpressionConfigurationConstant>, Set<Set<String>>> entry2,
                                     Set<String> pivotOptionsConvenient, Set<Set<String>> configurationsToExecute) {
        Iterator<Set<String>> set1 = entry1.getValue().iterator();
        Iterator<Set<String>> set2 = entry2.getValue().iterator();

        System.out.println("entry 1 size: " + entry1.getValue().size());
        System.out.println("entry 2 size: " + entry2.getValue().size());

        if(entry1.getValue().size() <= entry2.getValue().size()) {
            while(set1.hasNext() && set2.hasNext()) {
                Set<String> configurationInSet1 = set1.next();
                System.out.println("Set1: " + configurationInSet1);

                Set<String> valuePivotOptionsInSet1 = new HashSet<>(configurationInSet1);
                valuePivotOptionsInSet1.retainAll(pivotOptionsConvenient);
                boolean merged = false;

                while(set2.hasNext()) {
                    Set<String> configurationInSet2 = set2.next();
                    System.out.println("Set2: " + configurationInSet2);

                    Set<String> valuePivotOptionsInSet2 = new HashSet<>(configurationInSet2);
                    valuePivotOptionsInSet2.retainAll(pivotOptionsConvenient);

                    if(valuePivotOptionsInSet1.equals(valuePivotOptionsInSet2)) {
                        System.out.println("Can compress");
                        Set<String> compressedConfiguration = new HashSet<>(configurationInSet1);
                        compressedConfiguration.addAll(configurationInSet2);
                        configurationsToExecute.add(compressedConfiguration);

                        entry2.getValue().remove(configurationInSet2);
                        merged = true;
                        break;
                    }
                }

                if(!merged) {
                    throw new RuntimeException("Could not merge the sets");
                }

                set2 = entry2.getValue().iterator();
            }
        }
        else {
            while(set1.hasNext() && set2.hasNext()) {
                Set<String> configurationInSet2 = set2.next();
                System.out.println("Set2: " + configurationInSet2);

                Set<String> valuePivotOptionsInSet2 = new HashSet<>(configurationInSet2);
                valuePivotOptionsInSet2.retainAll(pivotOptionsConvenient);
                boolean merged = false;

                while(set1.hasNext()) {
                    Set<String> configurationInSet1 = set1.next();
                    System.out.println("Set1: " + configurationInSet1);

                    Set<String> valuePivotOptionsInSet1 = new HashSet<>(configurationInSet1);
                    valuePivotOptionsInSet1.retainAll(pivotOptionsConvenient);

                    if(valuePivotOptionsInSet1.equals(valuePivotOptionsInSet2)) {
                        System.out.println("Can compress");
                        Set<String> compressedConfiguration = new HashSet<>(configurationInSet2);
                        compressedConfiguration.addAll(configurationInSet1);
                        configurationsToExecute.add(compressedConfiguration);

                        entry1.getValue().remove(configurationInSet1);
                        merged = true;
                        break;
                    }
                }

                if(!merged) {
                    throw new RuntimeException("Could not merge the sets");
                }

                set1 = entry1.getValue().iterator();
            }
        }

        while(set1.hasNext()) {
            configurationsToExecute.add(set1.next());
        }

        while(set2.hasNext()) {
            configurationsToExecute.add(set2.next());
        }
    }

    public static Statement instrumentProgramToTimeRelevantStatements(Statement program,
                                                                      Set<Statement> relevantStatements) {
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
