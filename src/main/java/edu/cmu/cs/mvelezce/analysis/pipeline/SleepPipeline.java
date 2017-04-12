package edu.cmu.cs.mvelezce.analysis.pipeline;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.analysis.interpreter.SleepInterpreter;
import edu.cmu.cs.mvelezce.analysis.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.analysis.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.analysis.taint.Region;
import edu.cmu.cs.mvelezce.analysis.taint.Regions;
import edu.cmu.cs.mvelezce.analysis.taint.TaintAnalysis;
import edu.cmu.cs.mvelezce.sleep.ast.expression.Expression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ExpressionConfigurationConstant;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.StatementIf;
import edu.cmu.cs.mvelezce.sleep.ast.statement.StatementSleep;
import edu.cmu.cs.mvelezce.sleep.ast.statement.StatementTimed;
import edu.cmu.cs.mvelezce.sleep.lexer.Lexer;
import edu.cmu.cs.mvelezce.sleep.parser.Parser;
import edu.cmu.cs.mvelezce.sleep.visitor.VisitorReplacer;
import edu.cmu.cs.mvelezce.sleep.visitor.VisitorReturner;

import java.util.*;

/**
 * TODO
 * Created by miguelvelez on 2/11/17.
 */
public class SleepPipeline extends Pipeline {

    private static List<Class<? extends Statement>> relevantStatementsClasses = new ArrayList<Class<? extends Statement>>() {
        {
            add(StatementSleep.class);
            add(StatementIf.class);
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
        Map<RegionSleep, Set<ExpressionConfigurationConstant>> relevantRegionsToOptions = SleepPipeline.getRelevantRegionsToOptions(instructionsToTainted);
        // Configuration compression (Language independent)
        Set<Set<ExpressionConfigurationConstant>> relevantSleepOptions = new HashSet<>(relevantRegionsToOptions.values());
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

//    public static Map<Set<String>, Integer> buildPerformanceTable(String program, Set<ExpressionConfigurationConstant> parameters) {
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//        Map<Statement, Set<ExpressionConfigurationConstant>> relevantRegionsToOptions = SleepPipeline.getRelevantRegionsToOptions(instructionsToTainted);
//        Set<Set<ExpressionConfigurationConstant>> relevantOptions = new HashSet<>(relevantRegionsToOptions.values());
//
//        Set<Set<String>> convenient = new HashSet<>();
//        for(Set<ExpressionConfigurationConstant> relevantOption : relevantOptions) {
//            convenient.add(sleepConfigurationSetToStringSet(relevantOption));
//        }
//
//        Set<Set<String>> configurationsToExecute = SleepPipeline.getConfigurationsToExecute(convenient);
//        ast = SleepPipeline.instrumentProgramToTimeRelevantRegions(ast, relevantRegionsToOptions.keySet());
//        Set<PerformanceEntry> measuredPerformance = SleepPipeline.measureConfigurationPerformance(ast, configurationsToExecute);
//
//        return SleepPipeline.predictPerformanceForAllConfigurations(parameters, measuredPerformance, relevantRegionsToOptions);
//    }

//    public static Map<Set<String>, Integer> predictPerformanceForAllConfigurations(Set<ExpressionConfigurationConstant> parameters, Set<PerformanceEntry> measuredPerformance, Map<Statement, Set<ExpressionConfigurationConstant>> relevantStatementsToOptions) {
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
//                        if(statement instanceof StatementIf) {
//                            statement = ((StatementIf) statement).getThenBlock();
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
            SleepInterpreter interpreter = new SleepInterpreter(ast);
            interpreter.evaluate(configuration);
            configurationsToPerformance.add(new PerformanceEntry(configuration, Regions.getRegions()));
            // TODO calculate the performance of other configurations and see, in the future if we can reduce the number of configurations we need to execute
        }

        return configurationsToPerformance;
    }

    public static Map<RegionSleep, Set<ExpressionConfigurationConstant>> getRelevantRegionsToOptions(Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted) {
        Map<RegionSleep, Set<ExpressionConfigurationConstant>> relevantRegionToOptions = new HashMap<>();

        for(Map.Entry<BasicBlock, Set<TaintAnalysis.PossibleTaint>> entry : instructionsToTainted.entrySet()) {
            if(SleepPipeline.relevantStatementsClasses.contains(entry.getKey().getStatement().getClass())) {
                RelevantRegionGetterVisitor performanceStatementVisitor = new RelevantRegionGetterVisitor(entry.getValue());
                Set<ExpressionConfigurationConstant> possibleTaintingConfigurations = performanceStatementVisitor.getRelevantInfo(entry.getKey().getStatement());

                if(!possibleTaintingConfigurations.isEmpty()) {
                    RegionSleep relevantRegion = new RegionSleep(entry.getKey().getStatement());
                    Regions.addRegion(relevantRegion);
                    relevantRegionToOptions.put(relevantRegion, possibleTaintingConfigurations);
                }
            }
        }

        return relevantRegionToOptions;
    }

    public static Statement instrumentProgramToTimeRelevantRegions(Statement program, Set<RegionSleep> relevantRegions) {
        Set<Statement> relevantStatements = new HashSet<>();

        for(RegionSleep regionSleep : relevantRegions) {
            relevantStatements.add(regionSleep.getStatement());
        }

        AddTimedVisitor addTimedVisitor = new AddTimedVisitor(relevantStatements);
        return program.accept(addTimedVisitor);
    }

//    public static Set<ExpressionConfigurationConstant> stringSetToSleepConfigurationSet(Set<String> stringOptionsSet) {
//        Set<ExpressionConfigurationConstant> optionSetConvenient = new HashSet<>();
//
//        for(String option : stringOptionsSet) {
//            optionSetConvenient.add(new ExpressionConfigurationConstant(option));
//        }
//
//        return optionSetConvenient;
//    }
//
//    public static Set<Set<ExpressionConfigurationConstant>> setOfStringSetsToSetOfSleepConfigurationSets(Set<Set<String>> setOfStringOptionsSets) {
//        Set<Set<ExpressionConfigurationConstant>> setOfOptionSetConvenient = new HashSet<>();
//
//        for(Set<String> optionSet : setOfStringOptionsSets) {
//            setOfOptionSetConvenient.add(SleepPipeline.stringSetToSleepConfigurationSet(optionSet));
//        }
//
//        return setOfOptionSetConvenient;
//    }

    public static Set<String> sleepConfigurationSetToStringSet(Set<ExpressionConfigurationConstant> sleepOptionsSet) {
        Set<String> optionSetConvenient = new HashSet<>();

        for(ExpressionConfigurationConstant option : sleepOptionsSet) {
            optionSetConvenient.add(option.getName());
        }

        return optionSetConvenient;
    }

    public static Set<Set<String>> setOfSleepConfigurationSetsToSetOfStringSets(Set<Set<ExpressionConfigurationConstant>> setOfSleepOptionsSet) {
        Set<Set<String>> setOfOptionSetConvenient = new HashSet<>();

        for(Set<ExpressionConfigurationConstant> optionSet : setOfSleepOptionsSet) {
            setOfOptionSetConvenient.add(sleepConfigurationSetToStringSet(optionSet));
        }

        return setOfOptionSetConvenient;
    }

//    public static Map<Statement, Set<String>> sleepConfigurationMapToStringMap(Map<Statement, Set<ExpressionConfigurationConstant>> relevantStatementsToOptions) {
//        Map<Statement, Set<String>> relevantStatementsToOptionsConvenient = new HashMap<>();
//
//        for(Map.Entry<Statement, Set<ExpressionConfigurationConstant>> entry : relevantStatementsToOptions.entrySet()) {
//            Set<String> relevantOptionsConvenient = new HashSet<>();
//
//            for(ExpressionConfigurationConstant relevantOption : entry.getValue()) {
//                relevantOptionsConvenient.add(relevantOption.getName());
//            }
//
//            relevantStatementsToOptionsConvenient.put(entry.getKey(), relevantOptionsConvenient);
//        }
//
//        return relevantStatementsToOptionsConvenient;
//    }

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


    public static class RegionSleep extends Region {
        private Statement statement;

        public RegionSleep(Statement statement) {
            this.statement = statement;
        }

        public Statement getStatement() { return this.statement; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            RegionSleep that = (RegionSleep) o;

            return statement.equals(that.statement);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + statement.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RegionSleep{" +
                    "statement=" + statement +
                    '}';
        }
    }

    private static class RelevantRegionGetterVisitor extends VisitorReturner {
        private Set<TaintAnalysis.PossibleTaint> taintedVariables;
        private Set<ExpressionConfigurationConstant> relevantOptions;

        public RelevantRegionGetterVisitor(Set<TaintAnalysis.PossibleTaint> taintedVariables) {
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
                        this.relevantOptions.addAll(taintedVariable.getConfigurations());
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
         */ // TODO check this
        @Override
        public Statement visitStatementIf(StatementIf statementIf) {
            if(this.relevantStatements.contains(statementIf)) {
                StatementTimed timedThenBlock = new StatementTimed(statementIf.getThenBlock());

                return new StatementIf(statementIf.getCondition(), timedThenBlock);
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

//    public static class PerformanceEntry {
//        private Set<String> configuration;
//        private Map<Statement, Integer> blockToTime;
//        private int totalTime;
//        private int baseTime; // TODO might be calculated in the outer class since it is the same for all
//
//        public PerformanceEntry(Set<String> configuration, Map<Statement, Integer> blockToTime, int totalTime) {
//            this.configuration = configuration;
//            this.blockToTime = blockToTime;
//            this.totalTime = totalTime;
//            this.baseTime = totalTime;
//
//            for(Map.Entry<Statement, Integer> entry : this.blockToTime.entrySet()) {
//                this.baseTime -= entry.getValue();
//            }
//        }
//
//        public Set<String> getConfiguration() { return this.configuration; }
//
//        public Map<Statement, Integer> getBlockToTime() { return this.blockToTime; }
//
//        public int getTotalTime() { return this.totalTime; }
//
//        public int getBaseTime() { return this.baseTime; }
//
//        // TODO this is used for testing puposes. Do we need to remove this for shipping?
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            PerformanceEntry that = (PerformanceEntry) o;
//
//            if (totalTime != that.totalTime) return false;
//            if (baseTime != that.baseTime) return false;
//            if (!configuration.equals(that.configuration)) return false;
//            return blockToTime.equals(that.blockToTime);
//        }
//
//        @Override
//        public int hashCode() {
//            int result = configuration.hashCode();
//            result = 31 * result + blockToTime.hashCode();
//            result = 31 * result + totalTime;
//            result = 31 * result + baseTime;
//            return result;
//        }
//
//        @Override
//        public String toString() {
//            return "PerformanceEntry{" +
//                    "configuration=" + configuration +
//                    ", blockToTime=" + blockToTime +
//                    ", totalTime=" + totalTime +
//                    '}';
//        }
//    }
}
