package edu.cmu.cs.mvelezce.tool.pipeline.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.Program;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.Expression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.VariableExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.IfStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.interpreter.TimedSleepInterpreter;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.ReturnerVisitor;
import edu.cmu.cs.mvelezce.sleep.lexer.Lexer;
import edu.cmu.cs.mvelezce.sleep.parser.Parser;
import edu.cmu.cs.mvelezce.sleep.statements.TimedProgram;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.region.SleepRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.TaintAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.CFG;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.simple.SimpleCompression;
import edu.cmu.cs.mvelezce.tool.instrumentation.sleep.InstrumentVisitor;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceModel;

import java.util.*;

/**
 * Created by miguelvelez on 2/11/17.
 */
public class SleepPipeline {

    private static List<Class<? extends Statement>> relevantStatementsClasses = new ArrayList<Class<? extends Statement>>() {
        {
            add(SleepStatement.class);
            add(IfStatement.class);
        }
    };

    public static PerformanceModel buildPerformanceModel(String programFile) {
//        // Reset
//        Regions.reset();

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(program);

        // Taint ProgramAnalysis (Language dependent)
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        Map<SleepRegion, Set<ConstantConfigurationExpression>> relevantRegionsToOptions = SleepPipeline.getRelevantRegionsToOptions(instructionsToTainted);

        // Configuration compression (Language independent)
        Set<Set<ConstantConfigurationExpression>> relevantSleepOptions = new HashSet<>(relevantRegionsToOptions.values());
        Set<Set<String>> relevantOptions = SleepPipeline.setOfSleepConfigurationSetsToSetOfStringSets(relevantSleepOptions);
        Compression compression = new SimpleCompression(programFile, relevantOptions);
        Set<Set<String>> configurationsToExecute = compression.compressConfigurations();

        // Instrumentation (Language dependent)
        TimedProgram timedProgram = SleepPipeline.instrumentRelevantRegions(program); //, relevantRegionsToOptions);
//        TimedProgram timedProgram = SleepPipeline.instrumentProgram(program);
        Set<PerformanceEntry> measuredPerformance = SleepPipeline.measureConfigurationPerformance(timedProgram, configurationsToExecute);
//        System.out.println("Executed configurations: " + configurationsToExecute.size());

        // Performance Model (Language independent)
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<SleepRegion, Set<ConstantConfigurationExpression>> entry : relevantRegionsToOptions.entrySet()) {
            SleepRegion sleepRegion = entry.getKey();
            Region region = new Region(sleepRegion.getRegionID());
//            Region region = Regions.getRegion(entry.getKey().getRegionID());
            Set<String> options = SleepPipeline.sleepConfigurationSetToStringSet(entry.getValue());
            regionsToOptions.put(region, options);
        }

//        return DefaultPerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
        // TODO
        return null;
    }

    public static Set<PerformanceEntry> measureConfigurationPerformance(TimedProgram timedProgram, Set<Set<String>> configurationsToExecute) {
        Set<PerformanceEntry> configurationsToPerformance = new HashSet<>();

        for(Set<String> configuration : configurationsToExecute) {
            // TODO use TimedVisitor type, but then you have to add evaluate method in interface
//            Regions.resetRegions();
            TimedSleepInterpreter interpreter = new TimedSleepInterpreter(timedProgram);
            interpreter.evaluate(configuration);

            if(!Regions.getExecutingRegions().isEmpty()) {
                throw new RuntimeException("There program finished executing, but there are methods in the execution stack that did not finish");
            }

//            configurationsToPerformance.add(new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram()));
            // TODO calculate the performancemodel of other configurations and see, in the future if we can reduce the number of configurations we need to execute
        }

        return configurationsToPerformance;
    }

    public static Map<SleepRegion, Set<ConstantConfigurationExpression>> getRelevantRegionsToOptions(Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted) {
        Map<SleepRegion, Set<ConstantConfigurationExpression>> relevantRegionToOptions = new HashMap<>();

        for(Map.Entry<BasicBlock, Set<TaintAnalysis.PossibleTaint>> entry : instructionsToTainted.entrySet()) {
            if(SleepPipeline.relevantStatementsClasses.contains(entry.getKey().getStatement().getClass())) {
                RelevantRegionGetterVisitor performanceStatementVisitor = new RelevantRegionGetterVisitor(entry.getValue());
                relevantRegionToOptions.putAll(performanceStatementVisitor.getRelevantInfo(entry.getKey().getStatement()));
            }
        }

        return relevantRegionToOptions;
    }

    /**
     * We do not pass the regions since that is already stored in Regions.
     *
     * @param program
     * @return
     */
    public static TimedProgram instrumentRelevantRegions(Program program) { //, Map<SleepRegion), Set<ConstantConfigurationExpression>> relevantRegionsToOptions) {
//        InstrumentVisitor addTimedVisitor = new InstrumentVisitor(relevantRegionsToOptions);
        InstrumentVisitor addTimedVisitor = new InstrumentVisitor();
        return (TimedProgram) program.accept(addTimedVisitor);
    }

    public static Set<String> sleepConfigurationSetToStringSet(Set<ConstantConfigurationExpression> sleepOptionsSet) {
        Set<String> optionSetConvenient = new HashSet<>();

        for(ConstantConfigurationExpression option : sleepOptionsSet) {
            optionSetConvenient.add(option.getName());
        }

        return optionSetConvenient;
    }

    public static Set<Set<String>> setOfSleepConfigurationSetsToSetOfStringSets(Set<Set<ConstantConfigurationExpression>> setOfSleepOptionsSet) {
        Set<Set<String>> setOfOptionSetConvenient = new HashSet<>();

        for(Set<ConstantConfigurationExpression> optionSet : setOfSleepOptionsSet) {
            setOfOptionSetConvenient.add(sleepConfigurationSetToStringSet(optionSet));
        }

        return setOfOptionSetConvenient;
    }

    // TODO move to outside
    private static class RelevantRegionGetterVisitor extends ReturnerVisitor {
        private Set<TaintAnalysis.PossibleTaint> taintedVariables;
        private Set<ConstantConfigurationExpression> relevantOptions;
        private Map<SleepRegion, Set<ConstantConfigurationExpression>> regionToOptions;

        public RelevantRegionGetterVisitor(Set<TaintAnalysis.PossibleTaint> taintedVariables) {
            this.taintedVariables = taintedVariables;
            this.relevantOptions = new HashSet<>();
            this.regionToOptions = new HashMap<>();
        }

        public Map<SleepRegion, Set<ConstantConfigurationExpression>> getRelevantInfo(Statement statement) {
            statement.accept(this);

            return this.regionToOptions;
        }

        @Override
        public Expression visitConstantConfigurationExpression(ConstantConfigurationExpression configurationExpression) {
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
            ifStatement.getCondition().accept(this);

            if(!this.relevantOptions.isEmpty()) {
                Statement statement = ifStatement.getThenBlock();
                SleepRegion region = new SleepRegion(statement);

//                Regions.addRegion(region); TODO
                this.regionToOptions.put(region, this.relevantOptions);
            }

            return null;
        }

        @Override
        public Void visitSleepStatement(SleepStatement sleepStatement) {
            sleepStatement.getTime().accept(this);

            if(!this.relevantOptions.isEmpty()) {
                SleepRegion region = new SleepRegion(sleepStatement);
//                Regions.addRegion(region); TODO
                this.regionToOptions.put(region, this.relevantOptions);
            }

            return null;
        }
    }
}
