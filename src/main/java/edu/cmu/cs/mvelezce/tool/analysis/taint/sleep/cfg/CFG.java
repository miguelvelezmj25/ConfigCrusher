package edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg;

import java.util.*;

/**
 * A Control Flow Graph built with BasicBlocks. This is build with a pseudo-adjacency list; it has a map of BasicBlocks
 * with a list of BasicBlock representing the edges. It has special entry and exit Basic Blocks to represent the entry
 * and exit.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class CFG {
    private BasicBlock entry;
    private BasicBlock exit;
    private Map<BasicBlock, List<BasicBlock>> edges;

    /**
     * Initializes a {@code CFG}.
     */
    public CFG() {
        this.entry = new BasicBlock("entry");
        this.exit = new BasicBlock("exit");
        this.edges = new HashMap<>();

        this.edges.put(this.entry, new ArrayList<>());
        this.edges.put(this.exit, new ArrayList<>());
    }

    /**
     * Add an edge to the CFG.
     * @param from
     * @param to
     */
    public void addEdge(BasicBlock from, BasicBlock to) {
        if(from == null) {
            throw new IllegalArgumentException("The from BasicBlock cannot be null");
        }

        if(to == null) {
            throw new IllegalArgumentException("The to BasicBlock cannot be null");
        }

        if(!this.edges.containsKey(from)) {
            this.edges.put(from, new ArrayList<>());
        }

        if(!this.edges.containsKey(to)) {
            this.edges.put(to, new ArrayList<>());
        }

        List<BasicBlock> basicBlockEdges = this.edges.get(from);
        basicBlockEdges.add(to);
    }

    /**
     * Returns a list of all predecessors of this BasicBlock.
     * @param basicBlock
     * @return
     */
    public List<BasicBlock> getPredecessors(BasicBlock basicBlock) {
        if(basicBlock == null) {
            throw new IllegalArgumentException("The basicBlock cannot be null");
        }

        List<BasicBlock> predecessors = new LinkedList<>();

        for (Map.Entry<BasicBlock, List<BasicBlock>> entry : this.edges.entrySet()) {
            if (entry.getValue().contains(basicBlock)) {
                predecessors.add(entry.getKey());
            }
        }

        return predecessors;
    }

    /**
     * Returns a list of all successors of this BasicBlock.
     * @param basicBlock
     * @return
     */
    public List<BasicBlock> getSuccessors(BasicBlock basicBlock) {
        if(basicBlock == null) {
            throw new IllegalArgumentException("The basicBlock cannot be null");
        }

        return this.edges.get(basicBlock);
    }

    /**
     * Returns the entry BasicBlock
     * @return
     */
    public BasicBlock getEntry() { return this.entry; }

    /**
     * Returns the exit BasicBlock
     * @return
     */
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
