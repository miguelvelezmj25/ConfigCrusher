package edu.cmu.cs.mvelezce.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep5 {

    public static final String FILENAME = Sleep5.class.getCanonicalName();
    public static final String PACKAGE = Sleep5.class.getPackage().getName();
    public static final String CLASS = Sleep5.class.getSimpleName();
    public static final String MAIN_METHOD = "main";

    // TODO corner case that has two returns
    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);
        if(a) {
            Thread.sleep(600);
            return;
        }
        else {
            Thread.sleep(700);
        }

        Thread.sleep(100);
        // Region program end
    }

}
