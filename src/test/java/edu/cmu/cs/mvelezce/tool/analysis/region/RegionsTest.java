package edu.cmu.cs.mvelezce.tool.analysis.region;

import org.junit.Test;

/**
 * Created by mvelezce on 7/11/17.
 */
public class RegionsTest {

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

        long overhead = 0;

        for(Region region : Regions.getExecutedRegionsTrace()) {
            overhead += region.getOverhead();
        }

        System.out.println(overhead / 1000000000.0);
    }

}