package edu.cmu.cs.mvelezce.analysis;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by mvelezce on 3/9/17.
 */
public class PerformanceModelTest {

    @Test
    public void testToString1() {
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
        configuration.add("B");
        block.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 1);

        blocks.add(block);

        PerformanceModel pm = new PerformanceModel(6, blocks);
        String performanceModel = "T = 6 + (3A) + (1B)";
        Assert.assertEquals(performanceModel, pm.toString());
    }

    @Test
    public void testToString2() {
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
        configuration.add("AB");
        block.put(configuration, 3);

        blocks.add(block);

        block = new HashMap<>();
        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("B");
        block.put(configuration, 1);

        blocks.add(block);

        PerformanceModel pm = new PerformanceModel(6, blocks);
        String performanceModel = "T = 6 + (3A v +3AB v +3B) + (1B)";
        Assert.assertEquals(performanceModel, pm.toString());
    }
}