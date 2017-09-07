package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 6/22/17.
 */
public class VarexJProcessor {

    public static Map<JavaRegion, Set<String>> parse() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setValidating(true);
//        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        File coverage = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/gpl/coverage2.xml");

        if(!coverage.exists()) {
            throw new RuntimeException("No file");
        }

        String src = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/gpl/src/";
        String[] extensions = {"java"};
        Collection<File> files = FileUtils.listFiles(new File(src), extensions, true);

        Document root = builder.parse(coverage);
        root.getDocumentElement().normalize();
        NodeList fileList = root.getElementsByTagName("File");

        List<Map<String, String>> queryResult = new ArrayList<>();

        for(int i = 0; i < fileList.getLength(); i++) {
            Node fileNode = fileList.item(i);
            Element fileElement = (Element) fileNode;
            NodeList methodList = fileElement.getElementsByTagName("Method");

            for(int j = 0; j < methodList.getLength(); j++) {
                Node methodNode = methodList.item(j);
                Element methodElement = (Element) methodNode;
                NodeList instructionList = methodElement.getElementsByTagName("instr");

                for(int k = 0; k < instructionList.getLength(); k++) {
                    Node instructionNode = instructionList.item(k);
                    Element instructionElement = (Element) instructionNode;

                    Map<String, String> entry = new HashMap<>();
                    String className = fileElement.getAttribute("name");

                    for(File file : files) {
                        if(file.getName().equals(className)) {
                            String packageName = file.getPath();
                            packageName = packageName.replace(src, "");
                            packageName = packageName.replace("/", ".");
                            packageName = packageName.replace("." + className, "");
                            entry.put("package", packageName);

                            break;
                        }
                    }

                    className = className.substring(0, className.lastIndexOf("."));
                    entry.put("class", className);

                    entry.put("index", instructionElement.getAttribute("index"));
                    entry.put("features", instructionElement.getAttribute("text"));
                    entry.put("method", methodElement.getAttribute("name"));
                    entry.put("instruction", instructionElement.getAttribute("name"));
                    queryResult.add(entry);
                }
            }
        }

//        for (int i = 0; i < fileList.getLength(); i++) {
//            Node fileNode = fileList.item(i);
//            Element fileElement = (Element) fileNode;
//
//            System.out.println("File " + fileElement.getAttribute("name"));
//            NodeList methodList = fileElement.getElementsByTagName("Method");
//
//            for (int j = 0; j < methodList.getLength(); j++) {
//                Node methodNode = methodList.item(j);
//                Element methodElement = (Element) methodNode;
//
//                System.out.println("Method " + methodElement.getAttribute("name"));
//                NodeList instructionList = methodElement.getElementsByTagName("instr");
//
//                for (int k = 0; k < instructionList.getLength(); k++) {
//                    Node instructionNode = instructionList.item(k);
//                    Element instructionElement = (Element) instructionNode;
//
//                    Map<String, String> entry = new HashMap<>();
//                    entry.put("index", instructionElement.getAttribute("index"));
//                    entry.put("features", instructionElement.getAttribute("text"));
//                }
//
//                System.out.println("");
//            }
////                System.out.println(eElement.getElementsByTagName("firstname").item(0).getTextContent());
////                System.out.println(eElement.getElementsByTagName("lastname").item(0).getTextContent());
//        }

        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
        String currentPackage = "";
        String currentClass = "";
        String currentMethod = "";
        JavaRegion currentJavaRegion;
        int currentBytecodeIndex = 0;
        String currentInstruction = "";
        Set<String> currentUsedTerms = new HashSet<>();
        Set<String> firstUsedTermsInMethod = new HashSet<>();
        boolean atStartOfMethod = false;

        Set<String> features = new HashSet<>();
        features.add("SHORTEST");
        features.add("WEIGHTED");

        for(Map<String, String> result : queryResult) {
            String usedTermsString = result.get("features");
            Set<String> usedTerms = new HashSet<>(Arrays.asList(usedTermsString.split(",")));
            boolean addRegion = false;

            for(String feature : features) {
                if(usedTerms.contains(feature)) {
                    addRegion = true;
                    break;
                }
            }

            if(!addRegion) {
                continue;
            }

            Set<String> featuresToRemove = new HashSet<>();

            for(String term : usedTerms) {
                if(!features.contains(term)) {
                    featuresToRemove.add(term);
                }
            }

            usedTerms.removeAll(featuresToRemove);

            int entryBytecodeIndexes = Integer.parseInt(result.get("index"));
            String entryPackage = result.get("package");
            String entryClass = result.get("class");
            String entryMethod = result.get("method");
            String entryInstruction = result.get("instruction");

            if(!currentPackage.equals(entryPackage) || !currentClass.equals(entryClass) || !currentMethod.equals(entryMethod)) {
                currentBytecodeIndex = 0;
                currentInstruction = entryInstruction;
                currentUsedTerms = new HashSet<>();
                firstUsedTermsInMethod = new HashSet<>();
                firstUsedTermsInMethod.addAll(usedTerms);
                currentPackage = entryPackage;
                currentClass = entryClass;
                currentMethod = entryMethod;
                atStartOfMethod = true;
            }
            else {
                atStartOfMethod = false;
            }

            if(!currentUsedTerms.equals(usedTerms)) {
                currentUsedTerms = new HashSet<>();
                currentUsedTerms.addAll(usedTerms);

                if(currentUsedTerms.equals(firstUsedTermsInMethod) && !atStartOfMethod) {
                    continue;
                }

                if(!currentInstruction.contains("if") && !atStartOfMethod && currentBytecodeIndex == (entryBytecodeIndexes - 1)) {
                    continue;
                }

                currentJavaRegion = new JavaRegion(entryPackage, entryClass, entryMethod, Math.max(0, entryBytecodeIndexes - 1));
                regionsToOptions.put(currentJavaRegion, new HashSet<>(currentUsedTerms));
                System.out.println(currentJavaRegion.getRegionPackage() + " " + currentJavaRegion.getRegionClass() + " " + currentJavaRegion.getRegionMethod() + " " + currentJavaRegion.getStartBytecodeIndex() + " with " + currentUsedTerms);
            }

            currentBytecodeIndex = entryBytecodeIndexes;
            currentInstruction = entryInstruction;
        }

        // TODO do this after compressing regions
        Set<JavaRegion> regionsWithSameFeatureInAllInstructions = new HashSet<>();

        for(JavaRegion javaRegion : regionsToOptions.keySet()) {
            Set<String> regionUsedTerms = new HashSet<>();

            for(Map<String, String> result : queryResult) {
                String entryPackage = result.get("package");
                String entryClass = result.get("class");
                String entryMethod = result.get("method");
                int index = Integer.parseInt(result.get("index"));

                if(!javaRegion.getRegionPackage().equals(entryPackage) || !javaRegion.getRegionClass().equals(entryClass) || !javaRegion.getRegionMethod().equals(entryMethod)) {
                    if(!regionUsedTerms.isEmpty()) {
                        break;
                    }

                    continue;
                }

                if(regionUsedTerms.isEmpty()) {
                    if(index != 0) {
                        break;
                    }

                    String usedTermsString = result.get("features");
                    regionUsedTerms = new HashSet<>(Arrays.asList(usedTermsString.split(",")));
                }

                String usedTermsString = result.get("features");
                Set<String> usedTerms = new HashSet<>(Arrays.asList(usedTermsString.split(",")));

                if(!regionUsedTerms.equals(usedTerms)) {
                    regionUsedTerms = new HashSet<>();
                    break;
                }
            }

            if(!regionUsedTerms.isEmpty()) {
                System.out.println(javaRegion.getRegionMethod());
                regionsWithSameFeatureInAllInstructions.add(javaRegion);
            }

        }

        for(JavaRegion javaRegion : regionsWithSameFeatureInAllInstructions) {
            regionsToOptions.remove(javaRegion);
        }

        return regionsToOptions;
    }

}
