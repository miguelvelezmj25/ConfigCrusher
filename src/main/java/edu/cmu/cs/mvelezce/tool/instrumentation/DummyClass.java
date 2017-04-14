package edu.cmu.cs.mvelezce.tool.instrumentation;

/**
 * Created by mvelezce on 3/31/17.
 */
public class DummyClass {

    private int methodNumber;

    public DummyClass(int methodNumber) {
        this.methodNumber = methodNumber;
    }

    public void execute() {
        switch(this.methodNumber) {
            case 1: this.inc1(1);
                    break;
            case 2: this.inc2(2);
                    break;
            case 3: this.inc3(3);
                    break;
            case 4: this.inc4(4);
                    break;
        }
    }

    public void inc1(int a) {
//        System.out.println("Mom");
        int result = a + 1;
        System.out.println(result);
    }

    public void inc2(int a) {
//        Timer.startTimer("MiguelId");
        int result = a + 1;
        System.out.println(result);
//        Timer.stopTimer("MiguelId");
    }

    public void inc3(int a) {
//        System.nanoTime();
        int result = a + 1;
        System.out.println(result);
//        System.nanoTime();
//        long finalTime = end - start;
    }

    public void inc4(int a) {
//        Region hold = new Region("edu.cmu.cs.mvelezce.analysis.instrumentation", "DummyClass", "inc3");
//        Region region = Regions.getRegion(hold);
//        region.startTime();
        int result = a + 1;
        System.out.println(result);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        region.endTime();
    }

}
