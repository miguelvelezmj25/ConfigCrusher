package edu.cmu.cs.mvelezce.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep9 {

    public static final String FILENAME = Sleep9.class.getCanonicalName();
    public static final String PACKAGE = Sleep9.class.getPackage().getName();
    public static final String CLASS = Sleep9.class.getSimpleName();
    public static final String MAIN_METHOD = "main";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);

        int i = 0;

        // Region A start
        int repeat;
        if(a) { // 24
            repeat = 5;
        }
        else {
            repeat = 10;
        }
        // Region A end

        // Region A start
        while(i < repeat) { // 41
            Thread.sleep(100);
            i++;
        }
        // Region A end

        // Region program end
    }

}
