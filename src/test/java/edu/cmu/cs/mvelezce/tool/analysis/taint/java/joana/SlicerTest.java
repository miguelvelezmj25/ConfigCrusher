package edu.cmu.cs.mvelezce.tool.analysis.taint.java.joana;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 6/24/17.
 */
public class SlicerTest {
//    @Test
//    public void testBuild() {
////        // Builds pdg and saves it into temp folder
////        String name = "Sleep1";
////        String className = "edu.cmu.cs.mvelezce";
////        BuildSDG.standardConcBuild("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy",
////                className + "." + name,
////                "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep19/" + name + ".pdg");
//    }
//
//    @Test
//    public void testSleep19() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep19/edu.cmu.cs.mvelezce.Sleep19.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep19.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(1, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep1() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep1/edu.cmu.cs.mvelezce.Sleep1.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep1.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
////        features.add("B");
//
////        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        List<String> criterionLabels = new ArrayList<>();
//        criterionLabels.add("v8 = v6.booleanValue()");
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(2, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep2() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep2/edu.cmu.cs.mvelezce.Sleep2.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep2.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(3, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep3() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep3/edu.cmu.cs.mvelezce.Sleep3.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep3.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//        features.add("B");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(6, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep4() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep4/edu.cmu.cs.mvelezce.Sleep4.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep4.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(2, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep5() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep5/edu.cmu.cs.mvelezce.Sleep5.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep5.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(2, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep7() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep7/edu.cmu.cs.mvelezce.Sleep7.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep7.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(3, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep8() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep8/edu.cmu.cs.mvelezce.Sleep8.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep8.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(3, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep9() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep9/edu.cmu.cs.mvelezce.Sleep9.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep9.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(3, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep10() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep10/edu.cmu.cs.mvelezce.Sleep10.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep10.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//        features.add("B");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(6, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep11() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep11/edu.cmu.cs.mvelezce.Sleep11.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep11.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(3, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep12() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep12/edu.cmu.cs.mvelezce.Sleep12.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep12.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(3, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep13() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep13/edu.cmu.cs.mvelezce.Sleep13.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep13.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(2, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep14() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep14/edu.cmu.cs.mvelezce.Sleep14.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep14.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//        features.add("B");
//        features.add("C");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(8, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep15() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep15/edu.cmu.cs.mvelezce.Sleep15.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep15.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//        features.add("B");
//        features.add("C");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(6, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep16() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep16/edu.cmu.cs.mvelezce.Sleep16.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep16.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//        features.add("B");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(7, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testSleep17() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep17/edu.cmu.cs.mvelezce.Sleep17.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep17.main";
//        String criterionFormat = "booleanValue()";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//        features.add("B");
//        features.add("C");
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//        Assert.assertEquals(features.size(), criterionLabels.size());
//
//        Map<String, String> featuresToLabels = new HashMap<>();
//
//        for(int i = 0; i < criterionLabels.size(); i++) {
//            featuresToLabels.put(features.get(i), criterionLabels.get(i));
//        }
//
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = Slicer.analyze(file, featuresToLabels);
//        Assert.assertEquals(8, partialRegionsToOptions.size());
//    }
//
//    @Test
//    public void testGetCriterionLabel() throws IOException {
//        String file = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep1/edu.cmu.cs.mvelezce.Sleep1.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep1.main";
//        String criterionFormat = "booleanValue()";
//
//        List<String> criterionLabels = Slicer.getCriterionLabel(file, entryPoint, criterionFormat);
//
//        Assert.assertEquals(1, criterionLabels.size());
//    }

}