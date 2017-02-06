package edu.cmu.cs.mvelezce.analysis.cfg;

import java.util.*;

/**
 * Created by miguelvelez on 2/2/17.
 */
public class CFG {
    private BasicBlock entry;
    private BasicBlock exit;
    private Map<BasicBlock, List<BasicBlock>> edges;


    public CFG() {
        this.entry = new BasicBlock("entry");
        this.exit = new BasicBlock("exit");
        this.edges = new HashMap<>();

        this.edges.put(this.entry, new ArrayList<>());
        this.edges.put(this.exit, new ArrayList<>());
    }

    public void addEdge(BasicBlock from, BasicBlock to) {
        // TODO check if the from and to are already in the edges set
        if(!this.edges.containsKey(from)) {
            this.edges.put(from, new ArrayList<>());
        }

        List<BasicBlock> basicBlockEdges = this.edges.get(from);
        basicBlockEdges.add(to);
    }

    public List<BasicBlock> getPredecessors(BasicBlock basicBlock) {
        List<BasicBlock> predecessors = new LinkedList<>();

        for (Map.Entry<BasicBlock, List<BasicBlock>> entry : this.edges.entrySet()) {
            if (entry.getValue().contains(basicBlock)) {
                predecessors.add(entry.getKey());
            }
        }

        return predecessors;
    }

    public List<BasicBlock> getSuccessors(BasicBlock basicBlock) {
        return this.edges.get(basicBlock);
    }

    public BasicBlock getEntry() { return this.entry; }

    public BasicBlock getExit() { return this.exit; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CFG cfg = (CFG) o;

        if (!entry.equals(cfg.entry)) return false;
        if (!exit.equals(cfg.exit)) return false;
        return edges.equals(cfg.edges);
    }

    @Override
    public int hashCode() {
        int result = entry.hashCode();
        result = 31 * result + exit.hashCode();
        result = 31 * result + edges.hashCode();
        return result;
    }

    @Override
    public String toString() { return edges.toString(); }
}
