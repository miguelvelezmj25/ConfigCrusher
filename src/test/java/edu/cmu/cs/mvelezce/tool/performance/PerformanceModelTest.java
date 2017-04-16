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
        List<Map<Set<String>, Integer>> blocks = new ArrayList<>();
        Map<Set<String>, Integer> block = new HashMap<>();
        Set<String> configuration = new HashSet<>();
        block.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        block.put(configuration, 3);

        blocks.add(block);

        block = new HashMap<>();
        configuration = new HashSet<>();
        block.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 1);

        blocks.add(block);

        PerformanceModel pm = new PerformanceModel(6, blocks);

        int performance = 6;
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
        List<Map<Set<String>, Integer>> blocks = new ArrayList<>();
        Map<Set<String>, Integer> block = new HashMap<>();
        Set<String> configuration = new HashSet<>();
        block.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        block.put(configuration, 3);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 3);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        block.put(configuration, 3);

        blocks.add(block);

        block = new HashMap<>();
        configuration = new HashSet<>();
        block.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 1);

        blocks.add(block);

        PerformanceModel pm = new PerformanceModel(6, blocks);

        int performance = 6;
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
        Map<Set<String>, Integer> configurationsToPerformace = new HashedMap<>();
        configurationsToPerformace.put(longestConfiguration, 0);

        // Memoization storage
        Map<Set<String>, Integer> memoizationStore = new HashedMap<>();

        // Influence
        int influence = 0;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore));
    }

    @Test
    public void testCalculateConfigurationInfluence2() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();
        longestConfiguration.add("A");

        // Configurations to performance
        Map<Set<String>, Integer> configurationsToPerformace = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 0);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 5);

        // Memoization storage
        Map<Set<String>, Integer> memoizationStore = new HashedMap<>();

        // Influence
        int influence = 5;

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
        Map<Set<String>, Integer> configurationsToPerformace = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 0);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 5);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configurationsToPerformace.put(configuration, 5);

        // {A, B} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationsToPerformace.put(configuration, 5);

        // Memoization storage
        Map<Set<String>, Integer> memoizationStore = new HashedMap<>();

        // Influence
        int influence = -5;

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
        Map<Set<String>, Integer> configurationsToPerformace = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 0);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 0);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configurationsToPerformace.put(configuration, 0);

        // {A, B} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationsToPerformace.put(configuration, 5);

        // Memoization storage
        Map<Set<String>, Integer> memoizationStore = new HashedMap<>();

        // Influence
        int influence = 5;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore));
    }

    @Test
    public void testCalculateConfigurationInfluence5() {
        // Longest configuration
        Set<String> longestConfiguration = new HashSet<>();
        longestConfiguration.add("A");

        // Configurations to performance
        Map<Set<String>, Integer> configurationsToPerformace = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        configurationsToPerformace.put(configuration, 3);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configurationsToPerformace.put(configuration, 5);

        // Memoization storage
        Map<Set<String>, Integer> memoizationStore = new HashedMap<>();

        // Influence
        int influence = 2;

        // Assert
        Assert.assertEquals(influence, PerformanceModel.calculateConfigurationInfluence(longestConfiguration, configurationsToPerformace, memoizationStore));
    }

    @Test
    public void testCalculateInfluence1() {
        Map<Set<String>, Integer> regionTable = new HashedMap<>();

        // Influences
        Map<Set<String>, Integer> configurationToInfluence = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        regionTable.put(configuration, 0);
        // Influence
        configurationToInfluence.put(configuration, 0);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        regionTable.put(configuration, 3);
        // Influence
        configurationToInfluence.put(configuration, 3);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        regionTable.put(configuration, 3);
        // Influence
        configurationToInfluence.put(configuration, 3);

        // {C} configuration
        configuration = new HashSet<>();
        configuration.add("C");
        regionTable.put(configuration, 3);
        // Influence
        configurationToInfluence.put(configuration, 3);

        // {AB} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        regionTable.put(configuration, 3);
        // Influence
        configurationToInfluence.put(configuration, -3);

        // {AC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        regionTable.put(configuration, 3);
        // Influence
        configurationToInfluence.put(configuration, -3);

        // {BC} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 3);
        // Influence
        configurationToInfluence.put(configuration, -3);

        // {ABC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 3);
        // Influence
        configurationToInfluence.put(configuration, 3);

        // Assert
        Assert.assertEquals(configurationToInfluence, PerformanceModel.calculateConfigurationsInfluence(regionTable));
    }

    @Test
    public void testCalculateInfluence2() {
        Map<Set<String>, Integer> regionTable = new HashedMap<>();

        // Influences
        Map<Set<String>, Integer> configurationToInfluence = new HashedMap<>();

        // {} configuration
        Set<String> configuration = new HashSet<>();
        regionTable.put(configuration, 2);
        // Influence
        configurationToInfluence.put(configuration, 2);

        // {A} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        regionTable.put(configuration, 3);
        // Influence
        configurationToInfluence.put(configuration, 1);

        // {B} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        regionTable.put(configuration, 4);
        // Influence
        configurationToInfluence.put(configuration, 2);

        // {C} configuration
        configuration = new HashSet<>();
        configuration.add("C");
        regionTable.put(configuration, 2);
        // Influence
        configurationToInfluence.put(configuration, 0);

        // {AB} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        regionTable.put(configuration, 6);
        // Influence
        configurationToInfluence.put(configuration, 1);

        // {AC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        regionTable.put(configuration, 6);
        // Influence
        configurationToInfluence.put(configuration, 3);

        // {BC} configuration
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 9);
        // Influence
        configurationToInfluence.put(configuration, 5);

        // {ABC} configuration
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        regionTable.put(configuration, 16);
        // Influence
        configurationToInfluence.put(configuration, 2);

        // Assert
        Assert.assertEquals(configurationToInfluence, PerformanceModel.calculateConfigurationsInfluence(regionTable));
    }
}