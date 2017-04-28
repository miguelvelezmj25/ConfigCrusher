package edu.cmu.cs.mvelezce.tool.compression;

import edu.cmu.cs.mvelezce.tool.Helper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by mvelezce on 4/28/17.
 */
public class SimpleTest {

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

    public static void checktOptionsPermuatationsToGetConfigurationsToExecute(Set<Set<String>> relevantOptionsSet) {
        Collection<List<Set<String>>> permutations = CollectionUtils.permutations(relevantOptionsSet);

        for(List<Set<String>> permutation : permutations) {
//            System.out.println("\nPermutation: " + permutation);
            Set<Set<String>> results = Simple.getConfigurationsToExecute(relevantOptionsSet);
            SimpleTest.checkConfigurationIsStatisfied(new HashSet<>(permutation), results);
        }
    }

    public static void checkConfigurationIsStatisfied(Set<Set<String>> relevantOptionsSet, Set<Set<String>> results) {
//            System.out.println(results);
        for(Set<String> relevantOptions : relevantOptionsSet) {
            Set<Set<String>> powerSet = Helper.getConfigurations(relevantOptions);

            for (Set<String> configuration : powerSet) {
//                    System.out.println("Want configuration: " + configuration + " from: " + relevantOptionsConvenient);
                boolean hasConfiguration = false;

                for (Set<String> result : results) {
                    if (SimpleTest.matches(result, configuration, relevantOptions)) {
                        hasConfiguration = true;
                        break;
                    }
                }

                Assert.assertTrue(hasConfiguration);
            }
        }
    }

    public static boolean matches(Set<String> result, Set<String> configuration, Set<String> relevantOptions) {
        Set<String> valueOfResultInRelevantOption = new HashSet<>(relevantOptions);
        valueOfResultInRelevantOption.retainAll(result);
        return valueOfResultInRelevantOption.equals(configuration);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline1() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("AB, AC, AD, BE");

        // Program
        String program = "test1";
        Set<Set<String>> outputSave = Simple.getConfigurationsToExecute(program, args, relevantOptionsSet);

        args = new String[0];
        Set<Set<String>> outputRead = Simple.getConfigurationsToExecute(program, args, relevantOptionsSet);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecute1() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("AB, AC, AD, BE");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute2() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("ABC, BCD");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute3() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("AB, BCD");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute4() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("AB, BC");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute5() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("AB, CDE");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute6() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("AB, AC, BC");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute7() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("AB, AC, AD, BC, BD");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute8() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("AB, AC, AD, BC, CD");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute9() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("ABC, CD, BD");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute10() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("ABC, CD");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute11() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("ABC, DEF");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute12() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute13() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("ABCD, ADXY, ABDX");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute14() {
        Set<Set<String>> relevantOptionsSet = SimpleTest.getOptionsSet("AB, AC, AD, BC, CD, BD");
        SimpleTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void filterOptions1() {
        Set<Set<String>> set = SimpleTest.getOptionsSet("AB, AC");
        Assert.assertEquals(set, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions2() {
        Set<Set<String>> set = SimpleTest.getOptionsSet("ABC, ACD");
        Assert.assertEquals(set, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions3() {
        Set<Set<String>> set = SimpleTest.getOptionsSet("AB, ABC");
        Set<Set<String>> result = SimpleTest.getOptionsSet("ABC");
        Assert.assertEquals(result, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions4() {
        Set<Set<String>> set = SimpleTest.getOptionsSet("AB, ABC, BCD, BC, DEF");
        Set<Set<String>> result = SimpleTest.getOptionsSet("ABC, BCD, DEF");
        Assert.assertEquals(result, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

}