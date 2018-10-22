package edu.cmu.cs.mvelezce.tool.analysis.region;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mvelezce on 4/19/17.
 */
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
    this.regionPackage = null;
    this.regionClass = null;
    this.regionMethod = null;
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

  // TODO this implementation might brake the implementation in other CC components
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JavaRegion that = (JavaRegion) o;

    if (startRegionIndex != that.startRegionIndex) {
      return false;
    }

    assert regionPackage != null;
    if (!regionPackage.equals(that.regionPackage)) {
      return false;
    }

    assert regionClass != null;
    if (!regionClass.equals(that.regionClass)) {
      return false;
    }
    assert regionMethod != null;
    return regionMethod.equals(that.regionMethod);
  }

  // TODO this implementation might brake the implementation in other CC components
  @Override
  public int hashCode() {
    assert regionPackage != null;
    int result = regionPackage.hashCode();
    assert regionClass != null;
    result = 31 * result + regionClass.hashCode();
    assert regionMethod != null;
    result = 31 * result + regionMethod.hashCode();
    result = 31 * result + startRegionIndex;
    return result;
  }

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

  public MethodBlock getStartMethodBlock() {
    return this.startMethodBlock;
  }

  public Set<MethodBlock> getEndMethodBlocks() {
    return endMethodBlocks;
  }

  public String getStartBlockID() {
    return startBlockID;
  }

  public Set<String> getEndBlocksIDs() {
    return endBlocksIDs;
  }

  public void setStartBlockID(String startBlockID) {
    this.startBlockID = startBlockID;
  }

  public void setEndBlocksIDs(Set<String> endBlocksIDs) {
    this.endBlocksIDs = endBlocksIDs;
  }

  public void setStartRegionIndex(int startRegionIndex) {
    this.startRegionIndex = startRegionIndex;
  }

  public void setStartMethodBlock(MethodBlock startMethodBlock) {
    this.startMethodBlock = startMethodBlock;
  }

  public void setEndMethodBlocks(Set<MethodBlock> endMethodBlocks) {
    this.endMethodBlocks = endMethodBlocks;
  }

  @Override
  public String toString() {
    return "JavaRegion{" +
        "regionPackage='" + this.regionPackage + '\'' +
        ", regionClass='" + this.regionClass + '\'' +
        ", regionMethod='" + this.regionMethod + '\'' +
        ", startRegionIndex=" + this.startRegionIndex +
        '}';
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
