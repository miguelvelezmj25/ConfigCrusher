package edu.cmu.cs.mvelezce.tool.instrumentation;

/**
 * Created by mvelezce on 4/3/17.
 */
public class Timer {



    public static void startTimer(String id) {
        System.out.println("Starting timer: " + id);
    int i=4;
    i++;
    if (i>5)
        System.out.println(i);
    return;
    }

    public static void stopTimer(String id) {
        System.out.println("Stopping timer: " + id);
    }
}
