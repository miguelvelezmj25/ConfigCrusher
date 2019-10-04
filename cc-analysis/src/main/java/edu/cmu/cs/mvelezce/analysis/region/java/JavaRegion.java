package edu.cmu.cs.mvelezce.analysis.region.java;

import edu.cmu.cs.mvelezce.analysis.region.Region;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;

import java.util.HashSet;
import java.util.Set;

public class JavaRegion extends Region {

  private final String regionPackage;
  private final String regionClass;
  private final String regionMethod;

  private int startRegionIndex;
  private String startBlockID;
  private Set<String> endBlocksIDs;
  private MethodBlock startMethodBlock;
  private Set<MethodBlock> endMethodBlocks;

  // Needed for saving and reading regions in json
  private JavaRegion() {
    this.regionPackage = "";
    this.regionClass = "";
    this.regionMethod = "";
  }

  private JavaRegion(Builder builder) {
    super(builder);
    this.regionPackage = builder.regionPackage;
    this.regionClass = builder.regionClass;
    this.regionMethod = builder.regionMethod;
    this.startRegionIndex = builder.startBytecodeIndex;
    this.startMethodBlock = builder.startMethodBlock;
    this.endMethodBlocks = builder.endMethodBlocks;
    this.startBlockID = builder.startBlockID;
    this.endBlocksIDs = builder.endBlocksIDs;
  }

  //  // TODO this implementation might brake the implementation in other CC components since they
  // are
  // saved in sets or maps, but the state changes, so the hashcode does not match anymore.
  //  @Override
  //  public boolean equals(Object o) {
  //    if (this == o) {
  //      return true;
  //    }
  //    if (o == null || getClass() != o.getClass()) {
  //      return false;
  //    }
  //
  //    JavaRegion that = (JavaRegion) o;
  //
  //    if (startRegionIndex != that.startRegionIndex) {
  //      return false;
  //    }
  //
  //    if (!regionPackage.equals(that.regionPackage)) {
  //      return false;
  //    }
  //
  //    if (!regionClass.equals(that.regionClass)) {
  //      return false;
  //    }
  //
  //    return regionMethod.equals(that.regionMethod);
  //  }
  //
  //  // TODO this implementation might brake the implementation in other CC components
  //  @Override
  //  public int hashCode() {
  //    int result = regionPackage.hashCode();
  //    result = 31 * result + regionClass.hashCode();
  //    result = 31 * result + regionMethod.hashCode();
  //    result = 31 * result + startRegionIndex;
  //    return result;
  //  }

  public String getRegionPackage() {
    return this.regionPackage;
  }

  public String getRegionClass() {
    return this.regionClass;
  }

  public String getRegionMethod() {
    return this.regionMethod;
  }

  public int getStartRegionIndex() {
    return this.startRegionIndex;
  }

  public void setStartRegionIndex(int startRegionIndex) {
    this.startRegionIndex = startRegionIndex;
  }

  public MethodBlock getStartMethodBlock() {
    return this.startMethodBlock;
  }

  public void setStartMethodBlock(MethodBlock startMethodBlock) {
    this.startMethodBlock = startMethodBlock;
  }

  public Set<MethodBlock> getEndMethodBlocks() {
    return endMethodBlocks;
  }

  public void setEndMethodBlocks(Set<MethodBlock> endMethodBlocks) {
    this.endMethodBlocks = endMethodBlocks;
  }

  public String getStartBlockID() {
    return startBlockID;
  }

  public void setStartBlockID(String startBlockID) {
    this.startBlockID = startBlockID;
  }

  public Set<String> getEndBlocksIDs() {
    return endBlocksIDs;
  }

  public void setEndBlocksIDs(Set<String> endBlocksIDs) {
    this.endBlocksIDs = endBlocksIDs;
  }

  @Override
  public String toString() {
    return "JavaRegion{"
        + "regionPackage='"
        + this.regionPackage
        + '\''
        + ", regionClass='"
        + this.regionClass
        + '\''
        + ", regionMethod='"
        + this.regionMethod
        + '\''
        + ", startRegionIndex="
        + this.startRegionIndex
        + '}';
  }

  public static class Builder extends Region.Builder {

    private final String regionPackage;
    private final String regionClass;
    private final String regionMethod;

    private int startBytecodeIndex = Integer.MIN_VALUE;
    private MethodBlock startMethodBlock = null;
    private Set<MethodBlock> endMethodBlocks = new HashSet<>();
    private String startBlockID = "";
    private Set<String> endBlocksIDs = new HashSet<>();

    public Builder(String regionID, String regionPackage, String regionClass, String regionMethod) {
      super(regionID);
      this.regionPackage = regionPackage;
      this.regionClass = regionClass;
      this.regionMethod = regionMethod;
    }

    public Builder(String regionPackage, String regionClass, String regionMethod) {
      this.regionPackage = regionPackage;
      this.regionClass = regionClass;
      this.regionMethod = regionMethod;
    }

    public Builder startBytecodeIndex(int startBytecodeIndex) {
      this.startBytecodeIndex = startBytecodeIndex;
      return this;
    }

    public Builder startMethodBlock(MethodBlock startMethodBlock) {
      this.startMethodBlock = startMethodBlock;
      return this;
    }

    public Builder endMethodBlocks(Set<MethodBlock> endMethodBlocks) {
      this.endMethodBlocks = endMethodBlocks;
      return this;
    }

    public Builder startBlockID(String startBlockID) {
      this.startBlockID = startBlockID;
      return this;
    }

    public Builder endBlocksIDs(Set<String> endBlocksIDs) {
      this.endBlocksIDs = endBlocksIDs;
      return this;
    }

    public JavaRegion build() {
      return new JavaRegion(this);
    }
  }
}
