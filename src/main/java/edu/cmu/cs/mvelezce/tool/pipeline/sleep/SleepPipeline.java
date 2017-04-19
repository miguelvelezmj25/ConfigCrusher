package edu.cmu.cs.mvelezce.tool.pipeline.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.Program;
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
import edu.cmu.cs.mvelezce.sleep.statements.TimedProgram;
import edu.cmu.cs.mvelezce.sleep.statements.TimedStatement;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
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

    public static PerformanceModel buildPerformanceModel(String programFile) {
        // Reset
        Regions.reset();
        PerformanceEntry.reset();

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(program);

        // Taint Analysis (Language dependent)
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        Map<SleepRegion, Set<ConfigurationExpression>> relevantRegionsToOptions = SleepPipeline.getRelevantRegionsToOptions(instructionsToTainted);

        // Configuration compression (Language independent)
        Set<Set<ConfigurationExpression>> relevantSleepOptions = new HashSet<>(relevantRegionsToOptions.values());
        Set<Set<String>> relevantOptions = SleepPipeline.setOfSleepConfigurationSetsToSetOfStringSets(relevantSleepOptions);
        Set<Set<String>> configurationsToExecute = SleepPipeline.getConfigurationsToExecute(relevantOptions);

        // Instrumentation (Language dependent)
        program = SleepPipeline.instrumentRelevantRegions(program, relevantRegionsToOptions);
        TimedProgram timedProgram = SleepPipeline.instrumentProgram(program);
        Set<PerformanceEntry> measuredPerformance = SleepPipeline.measureConfigurationPerformance(timedProgram, configurationsToExecute);
//        System.out.println("Executed configurations: " + configurationsToExecute.size());

        // Performance Model (Language independent)
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<SleepRegion, Set<ConfigurationExpression>> entry : relevantRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey());
            Set<String> options = SleepPipeline.sleepConfigurationSetToStringSet(entry.getValue());
            regionsToOptions.put(region, options);
        }

        return SleepPipeline.createPerformanceModel(measuredPerformance, regionsToOptions);
    }

    public static Set<PerformanceEntry> measureConfigurationPerformance(TimedProgram ttimedProgram, Set<Set<String>> configurationsToExecute) {
        Set<PerformanceEntry> configurationsToPerformance = new HashSet<>();

        for(Set<String> configuration : configurationsToExecute) {
            // TODO use TimedVisitor type, but then you have to add evaluate method in interface
            Regions.resetRegions();
            TimedSleepInterpreter interpreter = new TimedSleepInterpreter(ttimedProgram);
            interpreter.evaluate(configuration);
//            System.out.println();
            configurationsToPerformance.add(new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram()));
            // TODO calculate the performance of other configurations and see, in the future if we can reduce the number of configurations we need to execute
        }

        return configurationsToPerformance;
    }

    public static Map<SleepRegion, Set<ConfigurationExpression>> getRelevantRegionsToOptions(Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted) {
        Map<SleepRegion, Set<ConfigurationExpression>> relevantRegionToOptions = new HashMap<>();

        for(Map.Entry<BasicBlock, Set<TaintAnalysis.PossibleTaint>> entry : instructionsToTainted.entrySet()) {
            if(SleepPipeline.relevantStatementsClasses.contains(entry.getKey().getStatement().getClass())) {
                RelevantRegionGetterVisitor performanceStatementVisitor = new RelevantRegionGetterVisitor(entry.getValue());
                relevantRegionToOptions.putAll(performanceStatementVisitor.getRelevantInfo(entry.getKey().getStatement()));
//                Set<ConfigurationExpression> possibleTaintingConfigurations = performanceStatementVisitor.getRelevantInfo(entry.getKey().getStatement());
//
//                if(!possibleTaintingConfigurations.isEmpty()) {
//                    Statement statement = entry.getKey().getStatement();
//
//                    // If we only want to consider the then branch as the region
//                    if(statement instanceof IfStatement) {
//                        statement = ((IfStatement) statement).getThenBlock();
//                    }
//
//                    SleepRegion relevantRegion = new SleepRegion(statement);
//                    Regions.addRegion(relevantRegion);
//                    relevantRegionToOptions.put(relevantRegion, possibleTaintingConfigurations);
//                }
            }
        }

        return relevantRegionToOptions;
    }

    /**
     * We do not pass the regions since that is already stored in Regions.
     * @param program
     * @return
     */
    public static Program instrumentRelevantRegions(Program program, Map<SleepRegion, Set<ConfigurationExpression>> relevantRegionsToOptions) {
        AddTimedVisitor addTimedVisitor = new AddTimedVisitor(relevantRegionsToOptions);
        return (Program) program.accept(addTimedVisitor);
    }

    public static TimedProgram instrumentProgram(Program program) {
        TimedProgram timedProgram = new TimedProgram(program);
        SleepRegion region = new SleepRegion(timedProgram.getStatements());
        Regions.addProgram(region);

        return timedProgram;
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

    private static class RelevantRegionGetterVisitor extends ReturnerVisitor {
        private Set<TaintAnalysis.PossibleTaint> taintedVariables;
        private Set<ConfigurationExpression> relevantOptions;
        private Map<SleepRegion, Set<ConfigurationExpression>> regionToOptions;

        public RelevantRegionGetterVisitor(Set<TaintAnalysis.PossibleTaint> taintedVariables) {
            this.taintedVariables = taintedVariables;
            this.relevantOptions = new HashSet<>();
            this.regionToOptions = new HashMap<>();
        }

        public  Map<SleepRegion, Set<ConfigurationExpression>> getRelevantInfo(Statement statement) {
            statement.accept(this);

            return this.regionToOptions;
        }

        @Override
        public Expression visitConfigurationExpression(ConfigurationExpression configurationExpression) {
            this.relevantOptions.add(configurationExpression);

            return configurationExpression;
        }

        @Override
        public Expression visitVariableExpression(VariableExpression variableExpression) {
                for(TaintAnalysis.PossibleTaint taintedVariable : this.taintedVariables) {
                    if(taintedVariable.getVariable().equals(variableExpression)) {
                        this.relevantOptions.addAll(taintedVariable.getConfigurations());
                    }
                }

            return variableExpression;
        }

        @Override
        public Void visitIfStatement(IfStatement ifStatement) {
            // TODO should we visit the then branch?
            ifStatement.getCondition().accept(this);

            if(!this.relevantOptions.isEmpty()) {
                Statement statement = ifStatement.getThenBlock();

                SleepRegion region = new SleepRegion(statement);
                Regions.addRegion(region);
                this.regionToOptions.put(region, this.relevantOptions);
            }

            return null;
        }

        @Override
        public Void visitSleepStatement(SleepStatement sleepStatement) {
            sleepStatement.getTime().accept(this);

            if(!this.relevantOptions.isEmpty()) {
                SleepRegion region = new SleepRegion(sleepStatement);
                Regions.addRegion(region);
                this.regionToOptions.put(region, this.relevantOptions);
            }

            return null;
        }
    }

    /**
     * Concrete visitor that replaces statements with TimedStatement for measuring time
     */
    private static class AddTimedVisitor extends ReplacerVisitor {
        private Map<SleepRegion, Set<ConfigurationExpression>> relevantRegionsToOptions;
        private Stack<Set<ConfigurationExpression>> constraints;

        /**
         * Instantiate a {@code AddTimedVisitor}.
         */
        public AddTimedVisitor(Map<SleepRegion, Set<ConfigurationExpression>> relevantRegionsToOptions) {
            this.relevantRegionsToOptions = relevantRegionsToOptions;
            this.constraints = new Stack<>();
        }

        /**
         * Replace the thenBlock of a IfStatement if the entire statement is relevant.
         *
         * @param ifStatement
         * @return
         */ // TODO check this since this is where we might need to work on to get inner regions
        @Override
        public Statement visitIfStatement(IfStatement ifStatement) {
            SleepRegion oldRegion = new SleepRegion(ifStatement.getThenBlock());
            oldRegion = (SleepRegion) Regions.getRegion(oldRegion);

            if(oldRegion != null) {
                this.constraints.push(this.relevantRegionsToOptions.get(oldRegion));
            }

            IfStatement visitedIfStatement = (IfStatement) super.visitIfStatement(ifStatement);

            if(oldRegion != null) {
                this.constraints.pop();

                Regions.removeRegion(oldRegion);
                SleepRegion region = new SleepRegion(visitedIfStatement.getThenBlock());
                Regions.addRegion(region);

                if(!this.constraints.contains(this.relevantRegionsToOptions.get(oldRegion))) {
                    return new IfStatement(visitedIfStatement.getCondition(), new TimedStatement(visitedIfStatement.getThenBlock()));
                }
            }

            return visitedIfStatement;
        }

        /**
         * Replace the thenBlock of a IfStatement if the entire statement is relevant.
         *
         * @param sleepStatement
         * @return
         */
        @Override
        public Statement visitSleepStatement(SleepStatement sleepStatement) {
            SleepRegion oldRegion = new SleepRegion(sleepStatement);
            oldRegion = (SleepRegion) Regions.getRegion(oldRegion);

            if(oldRegion != null) {
                this.constraints.push(this.relevantRegionsToOptions.get(oldRegion));
            }

            Statement visitedSleepStatement = super.visitSleepStatement(sleepStatement);

            if(oldRegion != null) {
                this.constraints.pop();

                Regions.removeRegion(oldRegion);
                SleepRegion region = new SleepRegion(visitedSleepStatement);
                Regions.addRegion(region);

                if(!this.constraints.contains(this.relevantRegionsToOptions.get(oldRegion))) {
                    return new TimedStatement(visitedSleepStatement);
                }
            }

            return visitedSleepStatement;
        }

    }
}
