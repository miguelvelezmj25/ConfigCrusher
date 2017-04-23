package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mvelezce on 4/21/17.
 */
public abstract class JavaRegionClassTransformer extends ClassTransformerBase {

    public JavaRegionClassTransformer(String fileName) {
        super(fileName);
    }

    public static Set<JavaRegion> getRegionInClass(String regionClass) {
        return JavaRegionClassTransformer.getRegionInClass("", regionClass);
    }

    public static Set<JavaRegion> getRegionInClass(String regionPackage, String regionClass) {
        Set<JavaRegion> javaRegions = new HashSet<>();

        for(Region region :  Regions.getRegions()) {
            JavaRegion javaRegion = (JavaRegion) region;

            if(javaRegion.getRegionPackage().equals(regionPackage) && javaRegion.getRegionClass().equals(regionClass)) {
                javaRegions.add(javaRegion);
            }
        }

        return javaRegions;
    }

}
