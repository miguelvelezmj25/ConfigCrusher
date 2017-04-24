package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep {


    public static final String FILENAME = Sleep.class.getCanonicalName();
    public static final String PACKAGE = Sleep.class.getPackage().getName();
    public static final String CLASS = Sleep.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static final String METHOD_1 = "program1";

    public static void main(String[] args) throws InterruptedException, CloneNotSupportedException {
//        JavaRegion region = new JavaRegion(Sleep.PACKAGE, Sleep.CLASS, Sleep.MAIN_METHOD, 38, 39);
//        Regions.addRegion(region);
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);
        if(a) {
//            JavaRegion hold = new JavaRegion(Sleep.class.getPackage().getName(), Sleep.class.getSimpleName(), "METHOD_NAME", -12, 327);
//            Region region = Regions.getRegion(hold);
//            region.enter();
//             Region A start
            Thread.sleep(300);
//            region.exit();
            // Region A end
        }
        Thread.sleep(100);
        // Region program end
    }

    public static void program1(boolean A) throws InterruptedException {
        Integer c = new Integer(2);
        c = new Integer(3);
        System.out.println("program1");
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
