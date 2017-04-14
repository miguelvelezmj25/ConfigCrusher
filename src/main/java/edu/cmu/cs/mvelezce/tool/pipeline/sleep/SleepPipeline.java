package edu.cmu.cs.mvelezce.tool.pipeline.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.expression.ConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.Expression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.VariableExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.IfStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.interpreter.TimedSleepInterpreter;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.ReplacerVisitor;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.ReturnerVisitor;
import edu.cmu.cs.mvelezce.sleep.lexer.Lexer;
import edu.cmu.cs.mvelezce.sleep.parser.Parser;
import edu.cmu.cs.mvelezce.sleep.statements.TimedStatement;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.TaintAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.CFG;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.pipeline.Pipeline;

import java.util.*;

/**
 * TODO
 * Created by miguelvelez on 2/11/17.
 */
public class SleepPipeline extends Pipeline {

    private static List<Class<? extends Statement>> relevantStatementsClasses = new ArrayList<Class<? extends Statement>>() {
        {
            add(SleepStatement.class);
            add(IfStatement.class);
        }
    };

    public static PerformanceModel buildPerformanceModel(String program) {
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        // Taint Analysis (Language dependent)
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        Map<SleepRegion, Set<ConfigurationExpression>> relevantRegionsToOptions = SleepPipeline.getRelevantRegionsToOptions(instructionsToTainted);
        // Configuration compression (Language independent)
        Set<Set<ConfigurationExpression>> relevantSleepOptions = new HashSet<>(relevantRegionsToOptions.values());
        Set<Set<String>> relevantOptions = SleepPipeline.setOfSleepConfigurationSetsToSetOfStringSets(relevantSleepOptions);
        Set<Set<String>> configurationsToExecute = SleepPipeline.getConfigurationsToExecute(relevantOptions);
        // Instrumentation (Language dependent)
        ast = SleepPipeline.instrumentProgramToTimeRelevantRegions(ast, relevantRegionsToOptions.keySet());
        Set<PerformanceEntry> measuredPerformance = SleepPipeline.measureConfigurationPerformance(ast, configurationsToExecute);
        // Performance Model (Language independent)
//        return SleepPipeline.createPerformanceModel(measuredPerformance, relevantRegionsToOptions);
        // TODO
        return null;
    }

//    public static Map<Set<String>, Integer> buildPerformanceTable(String program, Set<ConfigurationExpression> parameters) {
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//        Map<Statement, Set<ConfigurationExpression>> relevantRegionsToOptions = SleepPipeline.getRelevantRegionsToOptions(instructionsToTainted);
//        Set<Set<ConfigurationExpression>> relevantOptions = new HashSet<>(relevantRegionsToOptions.values());
//
//        Set<Set<String>> convenient = new HashSet<>();
//        for(Set<ConfigurationExpression> relevantOption : relevantOptions) {
//            convenient.add(sleepConfigurationSetToStringSet(relevantOption));
//        }
//
//        Set<Set<String>> configurationsToExecute = SleepPipeline.getConfigurationsToExecute(convenient);
//        ast = SleepPipeline.instrumentProgramToTimeRelevantRegions(ast, relevantRegionsToOptions.keySet());
//        Set<PerformanceEntry> measuredPerformance = SleepPipeline.measureConfigurationPerformance(ast, configurationsToExecute);
//
//        return SleepPipeline.predictPerformanceForAllConfigurations(parameters, measuredPerformance, relevantRegionsToOptions);
//    }

//    public static Map<Set<String>, Integer> predictPerformanceForAllConfigurations(Set<ConfigurationExpression> parameters, Set<PerformanceEntry> measuredPerformance, Map<Statement, Set<ConfigurationExpression>> relevantStatementsToOptions) {
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//
//        Set<Set<String>> configurationSpace = Helper.getConfigurations(SleepPipeline.sleepConfigurationSetToStringSet(parameters));
//        Set<Set<String>> measuredConfigurations = new HashSet<>();
//
//        for(PerformanceEntry performanceEntry : measuredPerformance) {
//            configurationToPerformance.put(performanceEntry.getConfiguration(), performanceEntry.getTotalTime());
//            measuredConfigurations.add(performanceEntry.getConfiguration());
//        }
//
//        configurationSpace.removeAll(measuredConfigurations);
//
//        Map<Statement, Set<String>> relevantStatementsToOptionsConvenient = SleepPipeline.sleepConfigurationMapToStringMap(relevantStatementsToOptions);
//
//        for(Set<String> configuration : configurationSpace) {
//            Map<Statement, Integer> blockToTime = new HashMap<>();
//
//            for(Map.Entry<Statement, Set<String>> entry : relevantStatementsToOptionsConvenient.entrySet()) {
//                Set<String> configurationValueInRelevantBlockForConfiguration = new HashSet<>(configuration);
//                configurationValueInRelevantBlockForConfiguration.retainAll(entry.getValue());
//
//                int baseTime = -1;
//
//                for(PerformanceEntry performanceEntry : measuredPerformance) {
//                    Set<String> configurationValueInMeasuredConfiguration = new HashSet<>(performanceEntry.getConfiguration());
//                    configurationValueInMeasuredConfiguration.retainAll(entry.getValue());
//
//                    if(configurationValueInMeasuredConfiguration.equals(configurationValueInRelevantBlockForConfiguration)) {
//                        Statement statement = entry.getKey();
//
//                        if(statement instanceof IfStatement) {
//                            statement = ((IfStatement) statement).getThenBlock();
//                        }
//
//                        Integer time = performanceEntry.getBlockToTime().get(statement);
//
//                        if(time != null) {
//                            blockToTime.put(entry.getKey(), time);
//                        }
//                        else {
//                            blockToTime.put(entry.getKey(), 0);
//                        }
//
//                        baseTime = performanceEntry.getBaseTime();
//                        break;
//                    }
//                }
//
//                int totalTime = baseTime;
//
//                for(Integer blockTime : blockToTime.values()) {
//                    totalTime += blockTime;
//                }
//
//                configurationToPerformance.put(configuration, totalTime);
//            }
//        }
//
//        return configurationToPerformance;
//    }

    public static Set<PerformanceEntry> measureConfigurationPerformance(Statement ast, Set<Set<String>> configurationsToExecute) {
        Set<PerformanceEntry> configurationsToPerformance = new HashSet<>();

        for(Set<String> configuration : configurationsToExecute) {
            // TODO use TimedVisitor type, but then you have to add evaluate method in interface
            Regions.resetRegions();
            TimedSleepInterpreter interpreter = new TimedSleepInterpreter(ast);
            interpreter.evaluate(configuration);
            configurationsToPerformance.add(new PerformanceEntry(configuration, Regions.getRegions()));
            // TODO calculate the performance of other configurations and see, in the future if we can reduce the number of configurations we need to execute
        }

        return configurationsToPerformance;
    }

    public static Map<SleepRegion, Set<ConfigurationExpression>> getRelevantRegionsToOptions(Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted) {
        Map<SleepRegion, Set<ConfigurationExpression>> relevantRegionToOptions = new HashMap<>();

        for(Map.Entry<BasicBlock, Set<TaintAnalysis.PossibleTaint>> entry : instructionsToTainted.entrySet()) {
            if(SleepPipeline.relevantStatementsClasses.contains(entry.getKey().getStatement().getClass())) {
                RelevantRegionGetterVisitor performanceStatementVisitor = new RelevantRegionGetterVisitor(entry.getValue());
                Set<ConfigurationExpression> possibleTaintingConfigurations = performanceStatementVisitor.getRelevantInfo(entry.getKey().getStatement());

                if(!possibleTaintingConfigurations.isEmpty()) {
                    Statement statement = entry.getKey().getStatement();

                    // If we only want to consider the then branch as the region
//                    if(statement instanceof IfStatement) {
//                        statement = ((IfStatement) statement).getThenBlock();
//                    }

                    SleepRegion relevantRegion = new SleepRegion(statement);
                    Regions.addRegion(relevantRegion);
                    relevantRegionToOptions.put(relevantRegion, possibleTaintingConfigurations);
                }
            }
        }

        return relevantRegionToOptions;
    }

    public static Statement instrumentProgramToTimeRelevantRegions(Statement program, Set<SleepRegion> relevantRegions) {
        Set<Statement> relevantStatements = new HashSet<>();

        for(SleepRegion regionSleep : relevantRegions) {
            relevantStatements.add(regionSleep.getStatement());
        }

        AddTimedVisitor addTimedVisitor = new AddTimedVisitor(relevantStatements);
        return program.accept(addTimedVisitor);
    }

    public static Set<String> sleepConfigurationSetToStringSet(Set<ConfigurationExpression> sleepOptionsSet) {
        Set<String> optionSetConvenient = new HashSet<>();

        for(ConfigurationExpression option : sleepOptionsSet) {
            optionSetConvenient.add(option.getName());
        }

        return optionSetConvenient;
    }

    public static Set<Set<String>> setOfSleepConfigurationSetsToSetOfStringSets(Set<Set<ConfigurationExpression>> setOfSleepOptionsSet) {
        Set<Set<String>> setOfOptionSetConvenient = new HashSet<>();

        for(Set<ConfigurationExpression> optionSet : setOfSleepOptionsSet) {
            setOfOptionSetConvenient.add(sleepConfigurationSetToStringSet(optionSet));
        }

        return setOfOptionSetConvenient;
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

    private static class RelevantRegionGetterVisitor extends ReturnerVisitor {
        private Set<TaintAnalysis.PossibleTaint> taintedVariables;
        private Set<ConfigurationExpression> relevantOptions;

        public RelevantRegionGetterVisitor(Set<TaintAnalysis.PossibleTaint> taintedVariables) {
            this.taintedVariables = taintedVariables;
            this.relevantOptions = new HashSet<>();
        }

        public Set<ConfigurationExpression> getRelevantInfo(Statement statement) {
            statement.accept(this);

            return this.relevantOptions;
        }

        @Override
        public Expression visitConfigurationExpression(ConfigurationExpression expressionConfigurationConstant) {
            this.relevantOptions.add(expressionConfigurationConstant);

            return expressionConfigurationConstant;
        }

        @Override
        public Expression visitVariableExpression(VariableExpression expressionVariable) {
                for(TaintAnalysis.PossibleTaint taintedVariable : this.taintedVariables) {
                    if(taintedVariable.getVariable().equals(expressionVariable)) {
                        this.relevantOptions.addAll(taintedVariable.getConfigurations());
                    }
                }

            return expressionVariable;
        }

        @Override
        public Void visitIfStatement(IfStatement statementIf) {
            statementIf.getCondition().accept(this);

            return null;
        }

        @Override
        public Void visitSleepStatement(SleepStatement statementSleep) {
            statementSleep.getTime().accept(this);

            return null;
        }
    }

    /**
     * Concrete visitor that replaces statements with TimedStatement for measuring time
     */
    private static class AddTimedVisitor extends ReplacerVisitor {
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
         * Replace the thenBlock of a IfStatement if the entire statement is relevant.
         *
         * @param statementIf
         * @return
         */ // TODO check this
        @Override
        public Statement visitIfStatement(IfStatement statementIf) {
            if(this.relevantStatements.contains(statementIf)) {
                TimedStatement timedThenBlock = new TimedStatement(statementIf.getThenBlock());

                return new IfStatement(statementIf.getCondition(), timedThenBlock);
            }

            return super.visitIfStatement(statementIf);
        }

        /**
         * Replace the thenBlock of a IfStatement if the entire statement is relevant.
         *
         * @param statementSleep
         * @return
         */
        @Override
        public Statement visitSleepStatement(SleepStatement statementSleep) {
            if(relevantStatements.contains(statementSleep)) {
                return new TimedStatement(statementSleep);
            }

            return statementSleep;
        }

    }
}
