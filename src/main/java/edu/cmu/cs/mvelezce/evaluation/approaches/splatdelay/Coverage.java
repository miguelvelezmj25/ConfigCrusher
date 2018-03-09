package edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay;

import java.util.Set;

public class Coverage {
    private Set<String> config;
    private Set<Set<String>> covered;

   private Coverage() { }

   public Coverage(Set<String> config, Set<Set<String>> covered) {
       this.config = config;
       this.covered = covered;
   }

    public Set<String> getConfig() {
        return config;
    }

    public void setConfig(Set<String> config) {
        this.config = config;
    }

    public Set<Set<String>> getCovered() {
        return covered;
    }

    public void setCovered(Set<Set<String>> covered) {
        this.covered = covered;
    }
}
