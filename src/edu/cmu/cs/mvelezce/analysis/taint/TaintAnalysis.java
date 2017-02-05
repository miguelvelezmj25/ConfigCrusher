package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;

import java.util.*;

/**
 * Created by miguelvelez on 2/5/17.
 */
public class TaintAnalysis {

    private Map<ExpressionVariable, ExpressionVariable> taintedValues;
    private CFG cfg;
    private Queue<BasicBlock> worklist;

    public TaintAnalysis(CFG cfg) {
        this.taintedValues = new HashMap<>();
        this.cfg = cfg;
        this.worklist = new LinkedList();
    }

    public void analyze() {
        List<BasicBlock> entry = this.cfg.getSuccessors(this.cfg.getEntry());

        if(entry.size() > 1) {
            throw new IllegalArgumentException("The entry point of the CFG has more than 1 edge");
        }

        this.worklist.add(entry.get(0));

        while(!this.worklist.isEmpty()) {
            BasicBlock instruction = this.worklist.remove();
            System.out.println(instruction);

            List<BasicBlock> successors = this.cfg.getSuccessors(instruction);

            if(successors.size() > 2) {
                throw new IllegalArgumentException("We do not support switch statements yet");
            }

            if(successors.size() == 0) {
                continue;
            }

            BasicBlock possibleInstruction;

            if(successors.size() == 2) {
                // TRUE branch is in the 0 position
                possibleInstruction = successors.remove(0);

                if(!possibleInstruction.isSpecial()) {
                    this.worklist.add(possibleInstruction);
                }
            }

            possibleInstruction = successors.get(0);
            if(!possibleInstruction.isSpecial()) {
                this.worklist.add(possibleInstruction);
            }
        }
    }

    private void transfer() {
        // TODO
    }

    private void join() {
        // TODO
    }


}
