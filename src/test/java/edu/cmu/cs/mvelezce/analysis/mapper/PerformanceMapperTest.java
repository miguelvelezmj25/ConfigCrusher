package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by miguelvelez on 2/11/17.
 */
public class PerformanceMapperTest {

    @Test
    public void pruneConfigurations1() throws Exception {
        Set<String> parameters = new HashSet<>();
        parameters.add("A");
        parameters.add("B");

        PerformanceMapper mapper = new PerformanceMapper(parameters);

        Set<ExpressionConfigurationConstant> consider = new HashSet<>();
        consider.add(new ExpressionConfigurationConstant("A"));

        mapper.pruneConfigurations(consider);

        Set<Set<String>> configurations = new HashSet<>();
        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurations.add(configuration);

        Assert.assertEquals(configurations, mapper.getAllConfigurations());
    }

    @Test
    public void pruneConfigurations2() throws Exception {
        Set<String> parameters = new HashSet<>();
        parameters.add("A");
        parameters.add("B");
        parameters.add("C");
        parameters.add("D");

        PerformanceMapper mapper = new PerformanceMapper(parameters);

        Set<ExpressionConfigurationConstant> consider = new HashSet<>();
        consider.add(new ExpressionConfigurationConstant("A"));
        consider.add(new ExpressionConfigurationConstant("B"));

        mapper.pruneConfigurations(consider);

        Set<Set<String>> configurations = new HashSet<>();
        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configuration.add("D");
        configurations.add(configuration);

        Assert.assertEquals(configurations, mapper.getAllConfigurations());
    }

}