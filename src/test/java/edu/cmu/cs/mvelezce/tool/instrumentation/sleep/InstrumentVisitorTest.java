package edu.cmu.cs.mvelezce.tool.instrumentation.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.Program;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.VariableExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.BlockStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.pipeline.sleep.SleepPipeline;
import edu.cmu.cs.mvelezce.tool.analysis.region.SleepRegion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created by miguelvelez on 4/25/17.
 */
public class InstrumentVisitorTest {

    @Before
    public void before() {
        Regions.reset();
    }

    @Test
    public void testVisitProgram1() throws Exception {
        // Program
        Statement sleepStatement = new SleepStatement(new VariableExpression("a"));
        List<Statement> programStatements = new ArrayList<>();
        programStatements.add(sleepStatement);
        BlockStatement blockStatement = new BlockStatement(programStatements);
        Program program = new Program(blockStatement);

        // Region
        SleepRegion region = new SleepRegion(sleepStatement);
        Regions.addRegion(region);

        // Regions to options
        Map<SleepRegion, Set<ConstantConfigurationExpression>> relevantRegionsToOptions = new HashMap<>();
        Set<ConstantConfigurationExpression> options = new HashSet<>();
        options.add(new ConstantConfigurationExpression("A"));
        relevantRegionsToOptions.put(region, options);


        // Assert
//        SleepPipeline.instrumentRelevantRegions(program, relevantRegionsToOptions);
        Assert.assertNotEquals(program, SleepPipeline.instrumentRelevantRegions(program)); //, relevantRegionsToOptions));
    }

}