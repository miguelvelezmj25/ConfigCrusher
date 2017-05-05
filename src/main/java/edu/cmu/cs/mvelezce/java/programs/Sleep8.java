package edu.cmu.cs.mvelezce.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep8 {

    public static final String FILENAME = Sleep8.class.getCanonicalName();
    public static final String PACKAGE = Sleep8.class.getPackage().getName();
    public static final String CLASS = Sleep8.class.getSimpleName();
    public static final String MAIN_METHOD = "main";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);

        // Region A start
        int repeat;
        if(a) { // 20
            repeat = 5;
        }
        else {
            repeat = 10;
        }
        // Region A end

        int i = 0;

        // Region A start
        while(i < repeat) { // 42
            Thread.sleep(100);
            i++;
        }
        // Region A end

        // Region program end
    }

}
