package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.Helper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.apache.commons.lang.StringUtils;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Tag;

public class ASMBytecodeOffsetFinder {

  private final String pathToClasses;
  private final Map<MethodNode, ClassNode> methodNodeToClassNode;
  private final Map<String, List<String>> classToJavapResult = new HashMap<>();

  ASMBytecodeOffsetFinder(String pathToClasses, Map<MethodNode, ClassNode> methodNodeToClassNode) {
//    this.getClassTransformer().getPathToClasses()
    this.pathToClasses = pathToClasses;
    this.methodNodeToClassNode = methodNodeToClassNode;
  }

  AbstractInsnNode getASMInstFromCaller(Edge edge, SootMethod sootMethod, MethodNode methodNode) {
    Unit srcUnit = edge.srcUnit();
    int bytecodeIndex = this.getBytecodeIndex(srcUnit);

    return this.getASMInstruction(methodNode, sootMethod, bytecodeIndex);
  }

  public AbstractInsnNode getASMInstruction(MethodNode methodNode, SootMethod sootMethod,
      int bytecodeIndex) {
    List<String> javapResult = this.getJavapResult(this.methodNodeToClassNode.get(methodNode));
    String methodDeclaration = sootMethod.getDeclaration() + ";";
    int javapStartIndexOfMethod = 0;

    while (!javapResult.get(javapStartIndexOfMethod).trim().equals(methodDeclaration)) {
      javapStartIndexOfMethod++;
    }

    javapStartIndexOfMethod += +3;
    int instructionNumberInJavap = this
        .getInstructionNumberInJavap(javapResult, javapStartIndexOfMethod, bytecodeIndex);

    return this.getInsnInMethodNode(methodNode, instructionNumberInJavap);
  }

  private AbstractInsnNode getInsnInMethodNode(MethodNode methodNode,
      int instructionNumberInJavap) {
    InsnList instructionsList = methodNode.instructions;
    ListIterator<AbstractInsnNode> instructions = instructionsList.iterator();
    int instructionCounter = -1;

    while (instructions.hasNext()) {
      AbstractInsnNode instruction = instructions.next();

      if (instruction.getOpcode() < 0) {
        continue;
      }

      instructionCounter++;

      if (instructionCounter == instructionNumberInJavap) {
        return instruction;
      }
    }

    throw new RuntimeException("Could not find the instruction");
  }

  private int getInstructionNumberInJavap(List<String> javapResult, int javapStartIndexOfMethod,
      int bytecodeIndex) {
    int instructionNumberInJavap = 0;
    int currentBytecodeIndex = -1;

    for (int i = javapStartIndexOfMethod; i < javapResult.size(); i++) {
      String outputLine = javapResult.get(i);
      outputLine = outputLine.trim();

      if (outputLine.contains(" Code:")) {
        throw new RuntimeException(
            "We might have gone past the method we wanted to analyze without finding the instruction");
      }

      // Probably the end of a switch statement
      if (!outputLine.contains(":")) {
        continue;
      }

      String potentialInstruction = outputLine.substring(outputLine.indexOf(":") + 1).trim();

      // Probably offsets of switch statements
      if (StringUtils.isNumeric(potentialInstruction)) {
        continue;
      }

      int outputLineBytecodeIndex;
      String outputLineBytecodeIndexString = outputLine.substring(0, outputLine.indexOf(":"))
          .trim();

      if (StringUtils.isNumeric(outputLineBytecodeIndexString)) {
        outputLineBytecodeIndex = Integer.valueOf(outputLineBytecodeIndexString);
      }
      else {
        throw new RuntimeException("Handle this case, but it was not handled before?");
      }

      if (outputLineBytecodeIndex == bytecodeIndex) {
        break;
      }

      instructionNumberInJavap++;

      if (outputLineBytecodeIndex > currentBytecodeIndex) {
        currentBytecodeIndex = outputLineBytecodeIndex;
      }
      else {
        throw new RuntimeException("Handle this case, but how can it happen?");
      }
    }

    return instructionNumberInJavap;
  }

  public int getJavapStartIndex(MethodNode methodNode) {
    ClassNode classNode = this.methodNodeToClassNode.get(methodNode);
    List<String> javapResult = this.getJavapResult(classNode);
    String methodNameInJavap = methodNode.name;

    if (methodNameInJavap.startsWith("<init>")) {
      methodNameInJavap = classNode.name;
      methodNameInJavap = methodNameInJavap.replace("/", ".");
    }

    if (methodNameInJavap.startsWith("<clinit>")) {
      methodNameInJavap = "  static {};";
    }
    else {
      methodNameInJavap += "(";
    }

    int methodStartIndex = 0;

    // Check if signature matches
    for (String outputLine : javapResult) {
      if (outputLine.equals(methodNameInJavap)) {
        if (!outputLine.equals("  static {};")) {
          throw new RuntimeException("Check this case");
        }

        break;
      }
      else if (outputLine.contains(" " + methodNameInJavap)) {
        String javapDescriptor = javapResult.get(methodStartIndex + 1).trim();
        javapDescriptor = javapDescriptor.substring(javapDescriptor.indexOf(" ")).trim();

        if (javapDescriptor.equals(methodNode.desc)) {
          break;
        }
      }

      methodStartIndex++;
    }

    if (methodStartIndex == javapResult.size()) {
      throw new RuntimeException("The start of the javap result cannot be the size of the result");
    }

    return methodStartIndex;
  }

  public List<String> getJavapResult(ClassNode classNode) {
    String classPackage = RegionTransformer.getClassPackage(classNode);
    String className = RegionTransformer.getClassName(classNode);

    return this.getJavapResult(classPackage, className);
  }

  private List<String> getJavapResult(String classPackage, String className) {
    List<String> javapResult = this.classToJavapResult.get(className);

    if (javapResult == null) {
      javapResult = this.getNewJavapResult(classPackage, className);
      this.classToJavapResult.put(className, javapResult);
    }

    return javapResult;
  }

  private List<String> getNewJavapResult(String classPackage, String className) {
    List<String> javapResult = new ArrayList<>();

    try {
      ProcessBuilder builder = new ProcessBuilder();

      List<String> commandList = this.buildCommandAsList(classPackage, className);
      builder.command(commandList);

      Process process = builder.start();

      javapResult.addAll(getJavapOutput(process));
      Helper.processError(process);

      process.waitFor();
    }
    catch (IOException | InterruptedException ie) {
      throw new RuntimeException(
          "Could not get the decompiled class file for " + classPackage + "." + className);
    }

    if (javapResult.size() < 3) {
      System.out.println(javapResult);
      throw new RuntimeException("The output of javap is not expected");
    }

    return javapResult;
  }

  private List<String> getJavapOutput(Process process) throws IOException {
    List<String> javapResult = new ArrayList<>();
    BufferedReader inputReader =
        new BufferedReader(new InputStreamReader(process.getInputStream()));
    String string;

    while ((string = inputReader.readLine()) != null) {
      if (!string.isEmpty()) {
        javapResult.add(string);
      }
    }

    return javapResult;
  }

  private List<String> buildCommandAsList(String classPackage, String className) {
    List<String> commandList = new ArrayList<>();
    commandList.add("javap");
    commandList.add("-classpath");
    commandList.add(this.pathToClasses);
    commandList.add("-p");
    commandList.add("-c");
    commandList.add("-s");
    commandList.add(classPackage + "." + className);

    return commandList;
  }

  private int getBytecodeIndex(Unit srcUnit) {
    List<Integer> bytecodeIndexes = new ArrayList<>();

    for (Tag tag : srcUnit.getTags()) {
      if (tag instanceof BytecodeOffsetTag) {
        int bytecodeIndex = ((BytecodeOffsetTag) tag).getBytecodeOffset();
        bytecodeIndexes.add(bytecodeIndex);
      }
    }

    if (bytecodeIndexes.isEmpty()) {
      throw new RuntimeException("There must be a bytecode index tag");
    }

    int bytecodeIndex;

    if (bytecodeIndexes.size() == 1) {
      bytecodeIndex = bytecodeIndexes.get(0);
    }
    else {
      int index = bytecodeIndexes.indexOf(Collections.min(bytecodeIndexes));
      bytecodeIndex = bytecodeIndexes.get(index);
    }

    return bytecodeIndex;
  }

}
