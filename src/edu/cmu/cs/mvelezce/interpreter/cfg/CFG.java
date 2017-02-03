package edu.cmu.cs.mvelezce.interpreter.cfg;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by miguelvelez on 2/2/17.
 */
public class CFG {
    private BasicBlock entry;
    private BasicBlock exit;
    private Set<BasicBlock> basicBlockSet;

    public CFG() {
        this.basicBlockSet = new HashSet<>();
        this.entry = new BasicBlock("entry", null, null);
        this.exit = new BasicBlock("exit", null, null);
    }

    public List<BasicBlock> getSuccessors(BasicBlock basicBlock) {
        return basicBlock.getSuccessors();
    }

    public List<BasicBlock> getPredecessor(BasicBlock basicBlock) {
        return basicBlock.getPredecessors();
    }

    public BasicBlock getEntry() { return this.entry; }

    public BasicBlock getExit() { return this.exit; }

    public Set<BasicBlock> getBasicBlock() { return this.basicBlockSet; }
}
