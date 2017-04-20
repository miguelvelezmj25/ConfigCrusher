package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.tool.analysis.Region;

/**
 * Created by mvelezce on 4/19/17.
 */
public class JavaRegion extends Region {
    private String regionPackage;
    private String regionClass;
    private String regionMethod;
    // TODO What is a region in java and how to identify it and limit it
    private int startJimpleLine;
    private int endJimpleLine;
    private String unit;

    public JavaRegion(String regionPackage, String regionClass, String regionMethod, int startJimpleLine, int endJimpleLine) {
        super();
        this.regionPackage = regionPackage;
        this.regionClass = regionClass;
        this.regionMethod = regionMethod;
        this.startJimpleLine = startJimpleLine;
        this.endJimpleLine = endJimpleLine;
    }

    public JavaRegion(String regionPackage, String regionClass, String regionMethod, int startJimpleLine) {
        this(regionPackage, regionClass, regionMethod, startJimpleLine, Integer.MIN_VALUE);
    }

    public JavaRegion(String regionPackage, String regionClass, String regionMethod) {
        this(regionPackage, regionClass, regionMethod, Integer.MIN_VALUE);
    }

    public JavaRegion(String regionClass, String regionMethod) {
        this("", regionClass, regionMethod);
    }

    public JavaRegion(String regionMethod) {
        this("", "", regionMethod);
    }

    public JavaRegion() {
        this("", "", "");
    }

    @Override
    public Region clone() throws CloneNotSupportedException {
        // TODO implement
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaRegion that = (JavaRegion) o;

        if (startJimpleLine != that.startJimpleLine) return false;
        if (endJimpleLine != that.endJimpleLine) return false;
        if (regionPackage != null ? !regionPackage.equals(that.regionPackage) : that.regionPackage != null)
            return false;
        if (regionClass != null ? !regionClass.equals(that.regionClass) : that.regionClass != null) return false;
        return regionMethod.equals(that.regionMethod);
    }

    @Override
    public int hashCode() {
        int result = regionPackage != null ? regionPackage.hashCode() : 0;
        result = 31 * result + (regionClass != null ? regionClass.hashCode() : 0);
        result = 31 * result + regionMethod.hashCode();
        result = 31 * result + startJimpleLine;
        result = 31 * result + endJimpleLine;
        return result;
    }

    @Override
    public String toString() {
        return "JavaRegion{" +
                "regionPackage='" + regionPackage + '\'' +
                ", regionClass='" + regionClass + '\'' +
                ", regionMethod='" + regionMethod + '\'' +
                ", startJimpleLine=" + startJimpleLine +
                ", endJimpleLine=" + endJimpleLine +
                '}';
    }

    public String getRegionPackage() { return this.regionPackage; }

    public String getRegionClass() { return this.regionClass; }

    public String getRegionMethod() { return this.regionMethod; }

    public int getStartJimpleLine() { return this.startJimpleLine; }

    public int getEndJimpleLine() { return this.endJimpleLine; }

    public void setStartJimpleLine(int startJimpleLine) { this.startJimpleLine = startJimpleLine; }

    public void setEndJimpleLine(int endJimpleLine) { this.endJimpleLine = endJimpleLine; }
}
