package edu.cmu.cs.mvelezce.analysis.taint;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Region {

    private String regionPackage;
    private String regionClass;
    private String regionMethod;

    public Region(String regionPackage, String regionClass, String regionMethod) {
        this.regionPackage = regionPackage;
        this.regionClass = regionClass;
        this.regionMethod = regionMethod;
    }

    public Region(String regionClass, String regionMethod) {
        this("", regionClass, regionMethod);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Region region = (Region) o;

        if (regionPackage != null ? !regionPackage.equals(region.regionPackage) : region.regionPackage != null)
            return false;
        if (!regionClass.equals(region.regionClass)) return false;
        return regionMethod.equals(region.regionMethod);
    }

    @Override
    public int hashCode() {
        int result = regionPackage != null ? regionPackage.hashCode() : 0;
        result = 31 * result + regionClass.hashCode();
        result = 31 * result + regionMethod.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Region{" +
                "regionPackage=" + '"' + regionPackage + '"' +
                ", regionClass=" + '"' + regionClass + '"' +
                ", regionMethod=" + '"' + regionMethod + '"' +
                '}';
    }

    public String getRegionPackage() { return this.regionPackage; }

    public String getRegionClass() { return this.regionClass; }

    public String getRegionMethod() { return this.regionMethod; }
}
