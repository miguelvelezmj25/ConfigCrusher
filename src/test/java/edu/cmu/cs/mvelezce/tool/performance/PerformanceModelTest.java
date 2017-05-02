package edu.cmu.cs.mvelezce.tool.performance;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by mvelezce on 3/9/17.
 */
public class PerformanceModelTest {

    @Test
    public void testEvaluate1() {
        List<Map<Set<String>, Double>> blocks = new ArrayList<>();
        Map<Set<String>, Double> block = new HashMap<>();
        Set<String> configuration = new HashSet<>();
        block.put(configuration, 0.0);

        configuration = new HashSet<>();
        configuration.add("A");
        block.put(configuration, 3.0);

        blocks.add(block);

        block = new HashMap<>();
        configuration = new HashSet<>();
        block.put(configuration, 0.0);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 1.0);

        blocks.add(block);

        PerformanceModel pm = new PerformanceModel(6, blocks);

        long performance = 6;
        configuration = new HashSet<>();
        Assert.assertEquals(performance, pm.evaluate(configuration), 0);

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, pm.evaluate(configuration), 0);

        performance = 9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, pm.evaluate(configuration), 0);

        performance = 7;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, pm.evaluate(configuration), 0);
    }

    @Test
    public void testEvaluate2() {
        List<Map<Set<String>, Double>> blocks = new ArrayList<>();
        Map<Set<String>, Double> block = new HashMap<>();
        Set<String> configuration = new HashSet<>();
        block.put(configuration, 0.0);

        configuration = new HashSet<>();
        configuration.add("A");
        block.put(configuration, 3.0);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 3.0);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        block.put(configuration, 3.0);

        blocks.add(block);

        block = new HashMap<>();
        configuration = new HashSet<>();
        block.put(configuration, 0.0);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 1.0);

        blocks.add(block);

        PerformanceModel pm = new PerformanceModel(6, blocks);

        long performance = 6;
        configuration = new HashSet<>();
        Assert.assertEquals(performance, pm.evaluate(configuration), 0);

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, pm.evaluate(configuration), 0);

        performance = 9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, pm.evaluate(configuration), 0);

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, pm.evaluate(configuration), 0);
    }

    @Test
    public void testCalculateConfigurationInfluence1() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();

        // Configurations to performance
        Map<Set<String>, Double> configurationsToPerformace = new HashMap<>();
        configurationsToPerformace.put(longestConfiguration, 0.0);

        // Memoization storage
        Map<Set<String>, Double> memoizationStore = new HashMap<>();

        // Influence
        double influence = 0.0;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore), 0);
    }

    @Test
    public void testCalculateConfigurationInfluence2() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();
        longestConfiguration.add("A");

        // Configurations to performance
        Map<Set<String>, Double> configurationsToPerformace = new HashMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 0.0);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 5.0);

        // Memoization storage
        Map<Set<String>, Double> memoizationStore = new HashMap<>();

        // Influence
        double influence = 5;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore), 0);
    }

    @Test
    public void testCalculateConfigurationInfluence3() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();
        longestConfiguration.add("A");
        longestConfiguration.add("B");

        // Configurations to performance
        Map<Set<String>, Double> configurationsToPerformace = new HashMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 0.0);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 5.0);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configurationsToPerformace.put(configuration, 5.0);

        // {A, B} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationsToPerformace.put(configuration, 5.0);

        // Memoization storage
        Map<Set<String>, Double> memoizationStore = new HashMap<>();

        // Influence
        double influence = -5;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore), 0);
    }

    @Test
    public void testCalculateConfigurationInfluence4() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();
        longestConfiguration.add("A");
        longestConfiguration.add("B");

        // Configurations to performance
        Map<Set<String>, Double> configurationsToPerformace = new HashMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 0.0);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 0.0);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configurationsToPerformace.put(configuration, 0.0);

        // {A, B} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationsToPerformace.put(configuration, 5.0);

        // Memoization storage
        Map<Set<String>, Double> memoizationStore = new HashMap<>();

        // Influence
        double influence = 5;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore), 0);
    }

    @Test
    public void testCalculateConfigurationInfluence5() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();
        longestConfiguration.add("A");

        // Configurations to performance
        Map<Set<String>, Double> configurationsToPerformace = new HashMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 3.0);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 5.0);

        // Memoization storage
        Map<Set<String>, Double> memoizationStore = new HashMap<>();

        // Influence
        double influence = 2;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore), 0);
    }

    @Test
    public void testCalculateInfluence1() {
        Map<Set<String>, Double> regionTable = new HashMap<>();

        // Influences
        Map<Set<String>, Double> configurationToInfluence = new HashMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        regionTable.put(configuration, 0.0);
        // Influence
        configurationToInfluence.put(configuration, 0.0);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        regionTable.put(configuration, 3.0);
        // Influence
        configurationToInfluence.put(configuration, 3.0);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        regionTable.put(configuration, 3.0);
        // Influence
        configurationToInfluence.put(configuration, 3.0);

        // {C} configuration
        configuration = new HashSet<>();
        configuration.add("C");
        regionTable.put(configuration, 3.0);
        // Influence
        configurationToInfluence.put(configuration, 3.0);

        // {AB} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        regionTable.put(configuration, 3.0);
        // Influence
        configurationToInfluence.put(configuration, -3.0);

        // {AC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        regionTable.put(configuration, 3.0);
        // Influence
        configurationToInfluence.put(configuration, -3.0);

        // {BC} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 3.0);
        // Influence
        configurationToInfluence.put(configuration, -3.0);

        // {ABC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 3.0);
        // Influence
        configurationToInfluence.put(configuration, 3.0);

        // Assert
        Assert.assertEquals(configurationToInfluence, PerformanceModel.calculateConfigurationsInfluence(regionTable));
    }

    @Test
    public void testCalculateInfluence2() {
        Map<Set<String>, Double> regionTable = new HashMap<>();

        // Influences
        Map<Set<String>, Double> configurationToInfluence = new HashMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        regionTable.put(configuration, 2.0);
        // Influence
        configurationToInfluence.put(configuration, 2.0);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        regionTable.put(configuration, 3.0);
        // Influence
        configurationToInfluence.put(configuration, 1.0);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        regionTable.put(configuration, 4.0);
        // Influence
        configurationToInfluence.put(configuration, 2.0);

        // {C} configuration
        configuration = new HashSet<>();
        configuration.add("C");
        regionTable.put(configuration, 2.0);
        // Influence
        configurationToInfluence.put(configuration, 0.0);

        // {AB} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        regionTable.put(configuration, 6.0);
        // Influence
        configurationToInfluence.put(configuration, 1.0);

        // {AC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        regionTable.put(configuration, 6.0);
        // Influence
        configurationToInfluence.put(configuration, 3.0);

        // {BC} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 9.0);
        // Influence
        configurationToInfluence.put(configuration, 5.0);

        // {ABC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 16.0);
        // Influence
        configurationToInfluence.put(configuration, 2.0);

        // Assert
        Assert.assertEquals(configurationToInfluence, PerformanceModel.calculateConfigurationsInfluence(regionTable));
    }


}