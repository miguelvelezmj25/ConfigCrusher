package edu.cmu.cs.mvelezce.tool.analysis.region;

import org.junit.Test;

/**
 * Created by mvelezce on 7/11/17.
 */
public class RegionsTest {

    public static void main(String[] args) {
        Regions.regionsToOverhead.put("a", 0L);
        Regions.regionsToOverhead.put("b", 0L);
        Regions.regionsToOverhead.put("c", 0L);
        Regions.regionsToOverhead.put("d", 0L);
        Regions.regionsToOverhead.put("e", 0L);
        int count = 2_000_000_00;
        long start = System.nanoTime();

        for(int i = 0; i < count; i++) {
            Regions.enter("a");
            Regions.enter("b");
            Regions.enter("c");

            Regions.exit("d");
            Regions.exit("e");

            Regions.exit("c");
            Regions.exit("b");
            Regions.exit("a");
        }

        long end = System.nanoTime();
        long time = end - start;

        System.out.println(time / 1000000000.0);
        System.out.println(time / count);
    }

//    @Test
//    public void main() throws Exception {
//            System.out.println("Started");
//            long start = System.nanoTime();
//
//            for(int i = 0; i < 96313444; i++) {
//                Regions.enter("dsf");
//                Regions.exit("dsf");
//            }
//
//            long end = System.nanoTime();
//            long time = end - start;
//
//            System.out.println("start count " + Regions.startCount);
//            System.out.println("end count " + Regions.endCount);
//            System.out.println(time / 1000000000.0);
//
//
//
//    }

    @Test
    public void testSleep1() throws InterruptedException {
        boolean A = false;
        Regions.enter("1");
        boolean var1;

        if(A) {
            var1 = true;
        }
        else {
            var1 = false;
        }

        Regions.exit("1");
        Thread.sleep(200L);
        Regions.enter("2");

        if(var1) {
            Thread.sleep(600L);
        }

        Regions.exit("2");
        Thread.sleep(100L);
    }

}