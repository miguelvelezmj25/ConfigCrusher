package edu.cmu.cs.mvelezce.analysis.instrumentation;

/**
 * Created by mvelezce on 4/3/17.
 */
public class Timer {

    public static void startTimer(String id) {
        System.out.println("Starting timer: " + id);
    }

    public static void stopTimer(String id) {
        System.out.println("Stopping timer: " + id);
    }
}
