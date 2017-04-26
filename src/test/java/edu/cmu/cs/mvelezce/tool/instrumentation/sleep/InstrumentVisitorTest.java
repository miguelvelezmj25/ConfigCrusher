package edu.cmu.cs.mvelezce.tool.instrumentation.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.Program;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.VariableExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.BlockStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.pipeline.sleep.SleepPipeline;
import edu.cmu.cs.mvelezce.tool.pipeline.sleep.SleepRegion;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by miguelvelez on 4/25/17.
 */
public class InstrumentVisitorTest {
    @Test
    public void testVisitProgram1() throws Exception {
        // Program
        Statement sleepStatement = new SleepStatement(new VariableExpression("a"));
        List<Statement> programStatements = new ArrayList<>();
        programStatements.add(sleepStatement);
        BlockStatement blockStatement = new BlockStatement(programStatements);
        Program program = new Program(blockStatement);

        // Region
        Regions.reset();
        SleepRegion region = new SleepRegion(sleepStatement);
        Regions.addRegion(region);

        // Regions to options
        Map<SleepRegion, Set<ConstantConfigurationExpression>> relevantRegionsToOptions = new HashedMap<>();
        Set<ConstantConfigurationExpression> options = new HashSet<>();
        options.add(new ConstantConfigurationExpression("A"));
        relevantRegionsToOptions.put(region, options);


        // Assert
//        SleepPipeline.instrumentRelevantRegions(program, relevantRegionsToOptions);
        Assert.assertNotEquals(program, SleepPipeline.instrumentRelevantRegions(program)); //, relevantRegionsToOptions));
    }

}