package edu.cmu.cs.mvelezce.tool.performance;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by mvelezce on 3/9/17.
 */
public class PerformanceModelTest {

    @Test
    public void testEvaluate1() {
        List<Map<Set<String>, Long>> blocks = new ArrayList<>();
        Map<Set<String>, Long> block = new HashMap<>();
        Set<String> configuration = new HashSet<>();
        block.put(configuration, 0L);

        configuration = new HashSet<>();
        configuration.add("A");
        block.put(configuration, 3L);

        blocks.add(block);

        block = new HashMap<>();
        configuration = new HashSet<>();
        block.put(configuration, 0L);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 1L);

        blocks.add(block);

        PerformanceModel pm = new PerformanceModel(6, blocks);

        long performance = 6;
        configuration = new HashSet<>();
        Assert.assertEquals(performance, pm.evaluate(configuration));

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, pm.evaluate(configuration));

        performance = 9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, pm.evaluate(configuration));

        performance = 7;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, pm.evaluate(configuration));
    }

    @Test
    public void testEvaluate2() {
        List<Map<Set<String>, Long>> blocks = new ArrayList<>();
        Map<Set<String>, Long> block = new HashMap<>();
        Set<String> configuration = new HashSet<>();
        block.put(configuration, 0L);

        configuration = new HashSet<>();
        configuration.add("A");
        block.put(configuration, 3L);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 3L);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        block.put(configuration, 3L);

        blocks.add(block);

        block = new HashMap<>();
        configuration = new HashSet<>();
        block.put(configuration, 0L);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 1L);

        blocks.add(block);

        PerformanceModel pm = new PerformanceModel(6, blocks);

        long performance = 6;
        configuration = new HashSet<>();
        Assert.assertEquals(performance, pm.evaluate(configuration));

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, pm.evaluate(configuration));

        performance = 9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, pm.evaluate(configuration));

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, pm.evaluate(configuration));
    }

    @Test
    public void testCalculateConfigurationInfluence1() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();

        // Configurations to performance
        Map<Set<String>, Long> configurationsToPerformace = new HashedMap<>();
        configurationsToPerformace.put(longestConfiguration, 0L);

        // Memoization storage
        Map<Set<String>, Long> memoizationStore = new HashedMap<>();

        // Influence
        long influence = 0;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore));
    }

    @Test
    public void testCalculateConfigurationInfluence2() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();
        longestConfiguration.add("A");

        // Configurations to performance
        Map<Set<String>, Long> configurationsToPerformace = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 0L);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 5L);

        // Memoization storage
        Map<Set<String>, Long> memoizationStore = new HashedMap<>();

        // Influence
        long influence = 5;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore));
    }

    @Test
    public void testCalculateConfigurationInfluence3() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();
        longestConfiguration.add("A");
        longestConfiguration.add("B");

        // Configurations to performance
        Map<Set<String>, Long> configurationsToPerformace = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 0L);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 5L);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configurationsToPerformace.put(configuration, 5L);

        // {A, B} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationsToPerformace.put(configuration, 5L);

        // Memoization storage
        Map<Set<String>, Long> memoizationStore = new HashedMap<>();

        // Influence
        long influence = -5;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore));
    }

    @Test
    public void testCalculateConfigurationInfluence4() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();
        longestConfiguration.add("A");
        longestConfiguration.add("B");

        // Configurations to performance
        Map<Set<String>, Long> configurationsToPerformace = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 0L);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 0L);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configurationsToPerformace.put(configuration, 0L);

        // {A, B} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationsToPerformace.put(configuration, 5L);

        // Memoization storage
        Map<Set<String>, Long> memoizationStore = new HashedMap<>();

        // Influence
        long influence = 5;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore));
    }

    @Test
    public void testCalculateConfigurationInfluence5() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();
        longestConfiguration.add("A");

        // Configurations to performance
        Map<Set<String>, Long> configurationsToPerformace = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 3L);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 5L);

        // Memoization storage
        Map<Set<String>, Long> memoizationStore = new HashedMap<>();

        // Influence
        long influence = 2;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore));
    }

    @Test
    public void testCalculateInfluence1() {
        Map<Set<String>, Long> regionTable = new HashedMap<>();

        // Influences
        Map<Set<String>, Long> configurationToInfluence = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        regionTable.put(configuration, 0L);
        // Influence
        configurationToInfluence.put(configuration, 0L);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        regionTable.put(configuration, 3L);
        // Influence
        configurationToInfluence.put(configuration, 3L);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        regionTable.put(configuration, 3L);
        // Influence
        configurationToInfluence.put(configuration, 3L);

        // {C} configuration
        configuration = new HashSet<>();
        configuration.add("C");
        regionTable.put(configuration, 3L);
        // Influence
        configurationToInfluence.put(configuration, 3L);

        // {AB} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        regionTable.put(configuration, 3L);
        // Influence
        configurationToInfluence.put(configuration, -3L);

        // {AC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        regionTable.put(configuration, 3L);
        // Influence
        configurationToInfluence.put(configuration, -3L);

        // {BC} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 3L);
        // Influence
        configurationToInfluence.put(configuration, -3L);

        // {ABC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 3L);
        // Influence
        configurationToInfluence.put(configuration, 3L);

        // Assert
        Assert.assertEquals(configurationToInfluence, PerformanceModel.calculateConfigurationsInfluence(regionTable));
    }

    @Test
    public void testCalculateInfluence2() {
        Map<Set<String>, Long> regionTable = new HashedMap<>();

        // Influences
        Map<Set<String>, Long> configurationToInfluence = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        regionTable.put(configuration, 2L);
        // Influence
        configurationToInfluence.put(configuration, 2L);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        regionTable.put(configuration, 3L);
        // Influence
        configurationToInfluence.put(configuration, 1L);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        regionTable.put(configuration, 4L);
        // Influence
        configurationToInfluence.put(configuration, 2L);

        // {C} configuration
        configuration = new HashSet<>();
        configuration.add("C");
        regionTable.put(configuration, 2L);
        // Influence
        configurationToInfluence.put(configuration, 0L);

        // {AB} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        regionTable.put(configuration, 6L);
        // Influence
        configurationToInfluence.put(configuration, 1L);

        // {AC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        regionTable.put(configuration, 6L);
        // Influence
        configurationToInfluence.put(configuration, 3L);

        // {BC} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 9L);
        // Influence
        configurationToInfluence.put(configuration, 5L);

        // {ABC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 16L);
        // Influence
        configurationToInfluence.put(configuration, 2L);

        // Assert
        Assert.assertEquals(configurationToInfluence, PerformanceModel.calculateConfigurationsInfluence(regionTable));
    }
}