package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.kit.joana.ifc.sdg.core.SecurityNode;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import edu.kit.joana.ifc.sdg.graph.slicer.SummarySlicerForward;

import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 6/24/17.
 */
public class Slicer {

    public static Map<JavaRegion, Set<String>> analyze(String file, Map<String, String> featuresToLabels) throws IOException {
        Map<JavaRegion, Set<String>> relevantRegionToOptions = new HashMap<>();
        SDG sdg = SDG.readFrom(file, new SecurityNode.SecurityNodeFactory());
        Set<SDGNode> nodes = sdg.vertexSet();

        for(Map.Entry<String, String> featureToLabel : featuresToLabels.entrySet()) {
            System.out.println("################################# " + featureToLabel.getKey());

            SDGNode criterion = null;

            for (SDGNode node : nodes) {
                if (node.getLabel().equals(featureToLabel.getValue())) {
                    criterion = node;
                }
            }

            SummarySlicerForward slicer = new SummarySlicerForward(sdg);
            Collection<SDGNode> slice = slicer.slice(criterion);
            Set<SDGNode> relevantNodes = new HashSet<>();

            for (SDGNode node : slice) {
                if(node.getLabel().startsWith("if ") && !node.getBytecodeMethod().startsWith("java.")) {
                    relevantNodes.add(node);
                }
            }

            for(SDGNode node : relevantNodes) {
                System.out.println(node.getBytecodeIndex());
                System.out.println(node.getLabel());
                if(node.getLabel().equals("cleanupTimeShifts")) {
                    int i = 0;
                }
                System.out.println(node.getBytecodeMethod());
                System.out.println(node.getBytecodeName());

                if(node.getBytecodeIndex() < 0) {
                    System.out.println("Skipped due to negative index");
                    continue;
                }
                System.out.println("");

                if(node.getBytecodeIndex() == 970) {
                    int i = 0;
                }

                String entryPackage = node.getBytecodeMethod();
//                entryPackage = entryPackage.substring(0, entryPackage.lastIndexOf("("));
                String entryMethod = entryPackage.substring(entryPackage.lastIndexOf(".") + 1);
                entryPackage = entryPackage.replace("." + entryMethod, "");
                String entryClass = entryPackage.substring(entryPackage.lastIndexOf(".") + 1);
                entryPackage = entryPackage.substring(0, entryPackage.lastIndexOf("." + entryClass));

                JavaRegion javaRegion = new JavaRegion(entryPackage, entryClass, entryMethod, node.getBytecodeIndex());
                boolean newRegion = true;

                for(Map.Entry<JavaRegion, Set<String>> regionToFeature : relevantRegionToOptions.entrySet()) {
                    JavaRegion relevantRegion = regionToFeature.getKey();

                    if(relevantRegion.getRegionPackage().equals(javaRegion.getRegionPackage())
                            && relevantRegion.getRegionClass().equals(javaRegion.getRegionClass())
                            && relevantRegion.getRegionMethod().equals(javaRegion.getRegionMethod())
                            && relevantRegion.getStartBytecodeIndex() == javaRegion.getStartBytecodeIndex()) {
                        regionToFeature.getValue().add(featureToLabel.getKey());
                        newRegion = false;
                        break;
                    }

                }

                if(newRegion) {
                    Set<String> features = new HashSet<>();
                    features.add(featureToLabel.getKey());
                    relevantRegionToOptions.put(javaRegion, features);
                }
            }

            System.out.println("");
        }

        return relevantRegionToOptions;
    }

    public static List<String> getCriterionLabel(String file,  String entryPoint, String criteriaFormat) throws IOException {
        List<String> criterionLabel = new ArrayList<>();
        SDG sdg = SDG.readFrom(file, new SecurityNode.SecurityNodeFactory());
        Set<SDGNode> nodes = sdg.vertexSet();
        List<SDGNode> entryPointNodes = new ArrayList<>();

        for(SDGNode node : nodes) {
            if(node.getBytecodeMethod().contains(entryPoint)) {
                entryPointNodes.add(node);
            }
        }

        entryPointNodes.sort(Comparator.comparingInt(SDGNode::getBytecodeIndex));

        for(SDGNode node : entryPointNodes) {
            if(node.getBytecodeMethod().contains(entryPoint) && node.getLabel().contains(criteriaFormat)) {
                criterionLabel.add(node.getLabel());
            }
        }

        return criterionLabel;
    }
}
