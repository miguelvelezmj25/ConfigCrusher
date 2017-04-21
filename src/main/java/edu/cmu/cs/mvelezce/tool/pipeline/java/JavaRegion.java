package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.tool.analysis.Region;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mvelezce on 4/19/17.
 */
/*
TODO JAVA regions are:
Method call
Then branch if statement
Else branch if statement
Block in while statement
Block in for statement
 */
public class JavaRegion extends Region {
    private String regionPackage;
    private String regionClass;
    private String regionMethod;
    private List<Integer> startBytecodeIndexes;
    private List<Integer> endBytecodeIndexes;

    public JavaRegion(String regionPackage, String regionClass, String regionMethod, List<Integer> startBytecodeIndexes, List<Integer> endBytecodeIndexes) {
        super();
        this.regionPackage = regionPackage;
        this.regionClass = regionClass;
        this.regionMethod = regionMethod;
        this.startBytecodeIndexes = startBytecodeIndexes;
        this.endBytecodeIndexes = endBytecodeIndexes;
    }


    public JavaRegion(String regionPackage, String regionClass, String regionMethod, List<Integer> startBytecodeIndexes) {
        this(regionPackage, regionClass, regionMethod, startBytecodeIndexes, new LinkedList<>());
    }

    public JavaRegion(String regionPackage, String regionClass, String regionMethod) {
        this(regionPackage, regionClass, regionMethod, new ArrayList<>(), new ArrayList<>());
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

        if (!regionPackage.equals(that.regionPackage)) return false;
        if (!regionClass.equals(that.regionClass)) return false;
        if (!regionMethod.equals(that.regionMethod)) return false;
        if (!startBytecodeIndexes.equals(that.startBytecodeIndexes)) return false;
        return endBytecodeIndexes.equals(that.endBytecodeIndexes);
    }

    @Override
    public int hashCode() {
        int result = regionPackage.hashCode();
        result = 31 * result + regionClass.hashCode();
        result = 31 * result + regionMethod.hashCode();
        result = 31 * result + startBytecodeIndexes.hashCode();
        result = 31 * result + endBytecodeIndexes.hashCode();
        return result;
    }

    public String getRegionPackage() { return this.regionPackage; }

    public String getRegionClass() { return this.regionClass; }

    public String getRegionMethod() { return this.regionMethod; }

    public List<Integer> getStartBytecodeIndexes() { return this.startBytecodeIndexes; }

    public List<Integer> getEndBytecodeIndexes() { return this.endBytecodeIndexes; }

    public void setStartBytecodeIndexes(List<Integer> startBytecodeIndexes) { this.startBytecodeIndexes = startBytecodeIndexes; }

    public void setEndBytecodeIndexes(List<Integer> endBytecodeIndexes) { this.endBytecodeIndexes = endBytecodeIndexes; }
}
