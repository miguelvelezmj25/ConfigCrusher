package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementAssignment;

import java.util.*;

/**
 * Created by miguelvelez on 2/5/17.
 */
public class TaintAnalysis {

// TODO Fix point loop
// TODO Worklist algorithm from cfg
// TODO Transfer function is to check how abstraction is changed from statement to statement
// TODO Join function is to join abstractions after they have branched out


//    private Map<ExpressionVariable, ExpressionVariable> taintedValues;
    private Set<ExpressionVariable> taintedValues;
    private CFG cfg;
    private Queue<BasicBlock> worklist;

    public TaintAnalysis(CFG cfg) {
//        this.taintedValues = new HashMap<>();
        this.taintedValues = new HashSet<>();
        this.cfg = cfg;
        this.worklist = new LinkedList<>();
    }

    public void analyze() {
        List<BasicBlock> entry = this.cfg.getSuccessors(this.cfg.getEntry());

        if(entry.size() > 1) {
            throw new IllegalArgumentException("The entry point of the CFG has more than 1 edge");
        }

        this.worklist.add(entry.get(0));

        while(!this.worklist.isEmpty()) {
            // Get the next available instruction
            BasicBlock instruction = this.worklist.remove();

            // Analyze
            System.out.println(instruction);
            if(instruction.getStatement() != null) {
                Statement statement = instruction.getStatement();

                if(statement instanceof StatementAssignment) {
                    StatementAssignment assignment = (StatementAssignment) statement;

                    if(this.taintedValues.contains(assignment.getRight())) {
                        this.taintedValues.add(assignment.getVariable());
                    }
                }
            }


            // Add next instructions to process
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
