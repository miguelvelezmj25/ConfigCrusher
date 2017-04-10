package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mvelezce on 4/10/17.
 */
public class PipelineTest {

    public static Set<Set<String>> getOptionsSet(String string) {
        Set<Set<String>> result = new HashSet<>();
        String[] allOptions = string.split(",");

        for(String options : allOptions) {
            Set<String> newOption = new HashSet<>();
            options = options.trim();

            for(int i = 0; i < options.length(); i++) {
                newOption.add(options.charAt(i) + "");
            }

            result.add(newOption);
        }

        return result;
    }

    public static void getConfigurationsToExecute(Set<Set<String>> relevantOptionsSet) {
        Collection<List<Set<String>>> permutations = CollectionUtils.permutations(relevantOptionsSet);

        for(List<Set<String>> permutation : permutations) {
//            System.out.println("\nPermutation: " + permutation);
            Set<Set<String>> permutationAsSet = new HashSet<>(permutation);
            Set<Set<String>> results = Pipeline.getConfigurationsToExecute(permutationAsSet);
//            System.out.println(results);

            for(Set<String> relevantOptions : relevantOptionsSet) {
                Set<Set<String>> powerSet = Helper.getConfigurations(relevantOptions);

                for (Set<String> configuration : powerSet) {
//                    System.out.println("Want configuration: " + configuration + " from: " + relevantOptionsConvenient);
                    boolean hasConfiguration = false;

                    for (Set<String> result : results) {
                        if (PipelineTest.matches(result, configuration, relevantOptions)) {
                            hasConfiguration = true;
                            break;
                        }
                    }

                    Assert.assertTrue(hasConfiguration);
                }
            }
        }
    }

    public static boolean matches(Set<String> result, Set<String> configuration, Set<String> relevantOptions) {
        Set<String> hold = new HashSet<>(relevantOptions);
        hold.retainAll(result);
        return hold.equals(configuration);
    }

    @Test
    public void testGetConfigurationsToExecute1() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, AC, AD, BE");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute2() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("ABC, BCD");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute3() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, BCD");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute4() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, BC");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute5() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, CDE");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute6() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, AC, BC");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute7() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, AC, AD, BC, BD");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute8() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, AC, AD, BC, CD");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test // TODO ERROR because I need to get the other 4 configs from ABC
    public void testGetConfigurationsToExecute9() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("ABC, CD, BD");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute10() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("ABC, CD");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute11() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("ABC, DEF");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute12() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute13() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("ABCD, ADXY, ABDX");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute14() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, AC, AD, BC, CD, BD");
        PipelineTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void filterOptions1() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("AB, AC");
        Assert.assertEquals(set, Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions2() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("ABC, ACD");
        Assert.assertEquals(set, Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions3() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("AB, ABC");
        Set<Set<String>> result = PipelineTest.getOptionsSet("ABC");
        Assert.assertEquals(result, Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions4() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("AB, ABC, BCD, BC, DEF");
        Set<Set<String>> result = PipelineTest.getOptionsSet("ABC, BCD, DEF");
        Assert.assertEquals(result, Pipeline.filterOptions(set));
    }

}