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
    
    public static Map<Set<String>, Integer> measurePerformance(String program, Set<ExpressionConfigurationConstant> parameters) {
        Map<Set<String>, Integer> measuredPerformanceMap = new HashMap<>();

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);

        Map<Statement, Set<ExpressionConfigurationConstant>> relevantStatementsToOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
        ast = PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, relevantStatementsToOptions.keySet());
        Set<Set<String>> configurationsToExecute = PerformanceMapper.getConfigurationsToExecute(relevantStatementsToOptions);
        Interpreter interpreter = new Interpreter(ast);

        Set<PerformanceEntry> configurationsToPerformance = new HashSet<>();

        for(Set<String> configuration : configurationsToExecute) {
            interpreter.evaluate(configuration);
            measuredPerformanceMap.put(configuration, interpreter.getTotalExecutionTime());

            configurationsToPerformance.add(new PerformanceEntry(configuration, interpreter.getTimedBlocks(), interpreter.getTotalExecutionTime()));

            // TODO calculate the performance of other configurations and see, in the future if we can reduce the number of configurations we need to execute
        }

        Set<Set<String>> powerSet = Helper.getConfigurations(parameters);
        Map<Set<String>, Integer> performanceMap = new HashMap<>();

        for(Set<String> configuration : powerSet) {
            System.out.println();
            System.out.println(configuration);

            if(measuredPerformanceMap.containsKey(configuration)) {
                performanceMap.put(configuration, measuredPerformanceMap.get(configuration));
                continue;
            }

            System.out.println("HAVE TO CALCULATE");
            Map<Statement, Integer> predictedBlockToTime = new HashMap<>();

            for(Map.Entry<Statement, Set<ExpressionConfigurationConstant>> entry : relevantStatementsToOptions.entrySet()) {
                Set<ExpressionConfigurationConstant> statementDependsOn = entry.getValue();
                Set<String> statementDependsOnConvenient = new HashSet<>();

                for(ExpressionConfigurationConstant option : statementDependsOn) {
                    statementDependsOnConvenient.add(option.getName());
                }

                for(PerformanceEntry performanceEntry : configurationsToPerformance) {

                    Set<String> a = new HashSet<>(statementDependsOnConvenient);
                    a.retainAll(configuration);

                    Set<String> b = new HashSet<>(performanceEntry.getConfiguration());
                    b.retainAll(statementDependsOnConvenient);

                    if(b.equals(a)) {
                        if(entry.getKey() instanceof StatementIf) {
                            StatementIf statementIf = (StatementIf) entry.getKey();
                            if(performanceEntry.getBlockToTime().containsKey(statementIf.getThenBlock())) {
                                predictedBlockToTime.put(statementIf, performanceEntry.getBlockToTime().get(statementIf.getThenBlock()));
                            }
                        }
                        else {
                            // TODO sleep statement
                        }
                        break;
                    }
                }
            }

            System.out.println(predictedBlockToTime);
            int totalTime = PerformanceEntry.getBaseTime();
            for(Map.Entry<Statement, Integer> entry : predictedBlockToTime.entrySet()) {
                totalTime += entry.getValue();
            }

            performanceMap.put(configuration, totalTime);
        }

        System.out.println();
        System.out.println("AND THE TABLE IS");
        System.out.println(performanceMap);

        return performanceMap;
    }

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

    public static Set<Set<String>> getConfigurationsToExecute(Map<Statement, Set<ExpressionConfigurationConstant>> relevantStatementToOptions) {
        Set<Set<ExpressionConfigurationConstant>> relevantOptions = new HashSet<>();

        // Calculates which options are included in other options
        for(Map.Entry <Statement, Set<ExpressionConfigurationConstant>> entry : relevantStatementToOptions.entrySet()) {
            if(relevantOptions.isEmpty()) {
                relevantOptions.add(entry.getValue());
                continue;
            }

            Set<Set<ExpressionConfigurationConstant>> toRemove = new HashSet<>();
            Set<Set<ExpressionConfigurationConstant>> toAdd = new HashSet<>();

            for(Set<ExpressionConfigurationConstant> options : relevantOptions) {
                if(options.equals(entry.getValue()) || options.containsAll(entry.getValue())) {
                    toAdd.remove(entry.getValue());
                    break;
                }

                if(!options.containsAll(entry.getValue()) && entry.getValue().containsAll(options)) {
                    toRemove.add(options);
                }

                toAdd.add(entry.getValue());

            }

            relevantOptions.removeAll(toRemove);
            relevantOptions.addAll(toAdd);
        }

        // Get the configurations for each option
        Map<Set<ExpressionConfigurationConstant>, Set<Set<String>>> optionsToConfigurationsToExecute = new HashMap<>();

        for(Set<ExpressionConfigurationConstant> option : relevantOptions) {
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
        Iterator<Set<String>> set1 = optionsToConfigurationsToExecuteIterator.next().getValue().iterator();

        while(optionsToConfigurationsToExecuteIterator.hasNext()) {
            configurationsToExecute = new HashSet<>();
            Iterator<Set<String>> set2 = optionsToConfigurationsToExecuteIterator.next().getValue().iterator();

            while (set1.hasNext() && set2.hasNext()) {
                Set<String> hold = new HashSet<>(set1.next());
                hold.addAll(set2.next());

                configurationsToExecute.add(hold);
            }

            while (set1.hasNext()) {
                configurationsToExecute.add(set1.next());
            }

            while (set2.hasNext()) {
                configurationsToExecute.add(set2.next());
            }

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
        private static int baseTime = -1; // TODO this looks weird

        public PerformanceEntry(Set<String> configuration, Map<Statement, Integer> blockToTime, int totalTime) {
            this.configuration = configuration;
            this.blockToTime = blockToTime;
            this.totalTime = totalTime;

            PerformanceEntry.calculateBaseTime(blockToTime,totalTime);
        }

        public Set<String> getConfiguration() { return this.configuration; }

        public Map<Statement, Integer> getBlockToTime() { return this.blockToTime; }

        public int getTotalTime() { return this.totalTime; }

        public static int getBaseTime() { return PerformanceEntry.baseTime; }

        private static void calculateBaseTime(Map<Statement, Integer> blockToTime, int totalTime) {
            PerformanceEntry.baseTime = totalTime;
            for(Map.Entry<Statement, Integer> entry : blockToTime.entrySet()) {
                PerformanceEntry.baseTime -= entry.getValue();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PerformanceEntry that = (PerformanceEntry) o;

            if (totalTime != that.totalTime) return false;
            if (!configuration.equals(that.configuration)) return false;
            return blockToTime.equals(that.blockToTime);
        }

        @Override
        public int hashCode() {
            int result = configuration.hashCode();
            result = 31 * result + blockToTime.hashCode();
            result = 31 * result + totalTime;
            return result;
        }

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
