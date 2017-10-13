package edu.cmu.cs.mvelezce.tool.analysis.region;

import java.util.UUID;

public class RegionID {

    private String ID;

    public RegionID(String regionID) {
        this.ID = regionID;
    }

    public RegionID() {
        this(UUID.randomUUID().toString());
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return this.ID;
    }
}
