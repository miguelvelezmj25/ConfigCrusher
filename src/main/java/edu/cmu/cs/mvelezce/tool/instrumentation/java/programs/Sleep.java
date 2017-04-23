package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep {

    public static final String FILENAME = Sleep.class.getCanonicalName();
    public static final String PACKAGE = Sleep.class.getPackage().getName();
    public static final String CLASS = Sleep.class.getSimpleName();
    public static final String METHOD_1 = "program1";

    public static void main(String[] args) throws InterruptedException {
        System.out.println("main");
        program1(Boolean.valueOf(args[0]));
        //        // Region program start
//        boolean a = Boolean.getBoolean(args[0]);
//        Thread.sleep(200);
//        if(a) {
//            // Region A start
//            Thread.sleep(300);
//            // Region A end
//        }
//        Thread.sleep(100);
//        // Region program end
    }

    public static void program1(boolean A) throws InterruptedException {
        System.out.println(A);
        boolean a = A;
        Thread.sleep(200);
        if(a) {
            // Region A start
            Thread.sleep(300);
            // Region A end
        }
        Thread.sleep(100);
    }

}
