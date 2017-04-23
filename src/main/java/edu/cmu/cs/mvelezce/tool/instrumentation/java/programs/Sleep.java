package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep {

    public static final String FILENAME = Sleep.class.getCanonicalName();
    public static final String PACKAGE = Sleep.class.getPackage().getName();
    public static final String CLASS = Sleep.class.getSimpleName();
    public static final String METHOD_1 = "program1";

    public void program1(boolean A) throws InterruptedException {
        // Region program start
        boolean a = A;
        Thread.sleep(200);
        if(a) {
            // Region A start
            Thread.sleep(300);
            // Region A end
        }
        Thread.sleep(100);
        // Region program end
    }

}
