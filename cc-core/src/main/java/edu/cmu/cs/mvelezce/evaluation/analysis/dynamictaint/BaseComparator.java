package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint;

public abstract class BaseComparator {

  static final String BASE_DIR = "./src/main/resources/evaluation/dynamicTaint/";
  static final String MISSING_DIR = "/missing/";
  static final String OVERLAPPING_DIR = "/overlapping/";

  protected static final String THREADS_DIR = "threads/";
  protected static final String WORKLOAD_DIR = "workload/";

  private final String programName;

  BaseComparator(String programName) {
    this.programName = programName;
  }

  String getProgramName() {
    return programName;
  }
}
