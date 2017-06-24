package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.kit.joana.api.IFCAnalysis;
import edu.kit.joana.api.sdg.SDGConfig;
import edu.kit.joana.ifc.sdg.core.SecurityNode;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import edu.kit.joana.ifc.sdg.graph.slicer.SummarySlicerForward;
import edu.kit.joana.ifc.sdg.graph.slicer.conc.simple.SimpleConcurrentSlicer;
import edu.kit.joana.wala.core.SDGBuilder;

import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 6/24/17.
 */
public class Slicer {

    public static void main(String[] args) throws IOException {
        SDG sdg = SDG.readFrom("/Users/mvelezce/Desktop/sleep1.pdg", new SecurityNode.SecurityNodeFactory());
        SummarySlicerForward slicer = new SummarySlicerForward(sdg);

        List<SDGNode> nodes = sdg.getNRandomNodes(sdg.vertexSet().size());
        List<SDGNode> myNodes = new ArrayList<>();

        for(SDGNode node : nodes) {
            if(node.getBytecodeMethod().contains("mvelezce")) {
                myNodes.add(node);
            }
        }

        SDGNode criterion = null;

        for(SDGNode node : myNodes) {
            if(node.getLabel().equals("v8 = v6.booleanValue()")) {
                criterion = node;
            }
        }

        Collection<SDGNode> slice = slicer.slice(criterion);
        Set<SDGNode> relevantNodes = new HashSet<>();

        for(SDGNode node : slice) {
            if(node.getLabel().contains("if")) {
                relevantNodes.add(node);
            }
        }

        for(SDGNode node : relevantNodes) {
            System.out.println(node.getBytecodeIndex());
            System.out.println(node.getLabel());
            System.out.println(node.getBytecodeMethod());
            System.out.println(node.getBytecodeName());
        }

    }



}
