package edu.cmu.cs.mvelezce.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep13 {

    public static final String FILENAME = Sleep13.class.getCanonicalName();
    public static final String PACKAGE = Sleep13.class.getPackage().getName();
    public static final String CLASS = Sleep13.class.getSimpleName();
    public static final String MAIN_METHOD = "main";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
//        boolean b = Boolean.valueOf(args[1]);
        Thread.sleep(200); if(a) { Thread.sleep(600); } /*if(b) { Thread.sleep(700); } */ Thread.sleep(100);
        // Region program end
    }

}
