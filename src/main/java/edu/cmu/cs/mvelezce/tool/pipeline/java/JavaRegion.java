package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mvelezce on 4/19/17.
 */
/*
JAVA regions are:
Method call
Then branch if statement
Else branch if statement
Block in while statement
Block in for statement
 */
public class JavaRegion extends Region {
    // Used for location
    private String regionPackage;
    private String regionClass;
    private String regionMethod;
    private int startBytecodeIndex;
    private int endBytecodeIndex;

    private JavaRegion(String regionId, String regionPackage, String regionClass, String regionMethod, int startBytecodeIndex, int endBytecodeIndex) {
        super(regionId);
        this.regionPackage = regionPackage;
        this.regionClass = regionClass;
        this.regionMethod = regionMethod;
        this.startBytecodeIndex = startBytecodeIndex;
        this.endBytecodeIndex = endBytecodeIndex;
    }

    public JavaRegion(String regionPackage, String regionClass, String regionMethod, int startBytecodeIndex, int endBytecodeIndex) {
        this.regionPackage = regionPackage;
        this.regionClass = regionClass;
        this.regionMethod = regionMethod;
        this.startBytecodeIndex = startBytecodeIndex;
        this.endBytecodeIndex = endBytecodeIndex;
    }

    public JavaRegion(String regionPackage, String regionClass, String regionMethod, int startBytecodeIndex) {
        this(regionPackage, regionClass, regionMethod, startBytecodeIndex, Integer.MIN_VALUE);
    }

    public JavaRegion(String regionPackage, String regionClass, String regionMethod) {
        this(regionPackage, regionClass, regionMethod, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
    public JavaRegion(String regionClass, String regionMethod) {
        this("", regionClass, regionMethod);
    }

    public JavaRegion() {
        this("", "");
    }

    // Todo Seems weird to have this here
    public static Set<JavaRegion> getRegionsInClass(String regionClass) {
        return JavaRegion.getRegionsInClass("", regionClass);
    }

    public static Set<JavaRegion> getRegionsInClass(String regionPackage, String regionClass) {
        Set<JavaRegion> javaRegions = new HashSet<>();

        for(Region region : Regions.getRegions()) {
            JavaRegion javaRegion = (JavaRegion) region;

            if(javaRegion.getRegionPackage().equals(regionPackage) && javaRegion.getRegionClass().equals(regionClass)) {
                javaRegions.add(javaRegion);
            }
        }

        return javaRegions;
    }

    @Override
    public Region clone() throws CloneNotSupportedException {
        return new JavaRegion(this.getRegionID(), this.regionPackage, this.regionClass, this.regionMethod, this.startBytecodeIndex, this.endBytecodeIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaRegion that = (JavaRegion) o;

        if (startBytecodeIndex != that.startBytecodeIndex) return false;
        if (endBytecodeIndex != that.endBytecodeIndex) return false;
        if (!regionPackage.equals(that.regionPackage)) return false;
        if (!regionClass.equals(that.regionClass)) return false;
        return regionMethod.equals(that.regionMethod);
    }

    @Override
    public int hashCode() {
        int result = regionPackage.hashCode();
        result = 31 * result + regionClass.hashCode();
        result = 31 * result + regionMethod.hashCode();
        result = 31 * result + startBytecodeIndex;
        result = 31 * result + endBytecodeIndex;
        return result;
    }

    @Override
    public String toString() {
        return "JavaRegion{" +
                "regionPackage='" + this.regionPackage + '\'' +
                ", regionClass='" + this.regionClass + '\'' +
                ", regionMethod='" + this.regionMethod + '\'' +
                ", startBytecodeIndex=" + this.startBytecodeIndex +
                ", endBytecodeIndex=" + this.endBytecodeIndex +
                '}';
    }

    public String getRegionPackage() { return this.regionPackage; }

    public String getRegionClass() { return this.regionClass; }

    public String getRegionMethod() { return this.regionMethod; }

    public int getStartBytecodeIndex() { return this.startBytecodeIndex; }

    public int getEndBytecodeIndex() { return this.endBytecodeIndex; }

    public void setStartBytecodeIndex(int startBytecodeIndex) { this.startBytecodeIndex = startBytecodeIndex; }

    public void setEndBytecodeIndex(int endBytecodeIndex) { this.endBytecodeIndex = endBytecodeIndex; }

}
