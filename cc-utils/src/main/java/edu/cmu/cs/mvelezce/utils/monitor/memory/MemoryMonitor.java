package edu.cmu.cs.mvelezce.utils.monitor.memory;

public final class MemoryMonitor {

  private MemoryMonitor() {}

  public static double getMemoryUsage() {
    return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1E9;
  }

  public static void printMemoryUsage(String prefix) {
    System.out.println(prefix + " " + getMemoryUsage() + " GB");
  }
}
