package edu.cmu.cs.mvelezce.analysis;

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
}