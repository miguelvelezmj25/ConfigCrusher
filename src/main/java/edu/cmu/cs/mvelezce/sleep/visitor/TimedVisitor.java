package edu.cmu.cs.mvelezce.sleep.visitor;

import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.Visitor;
import edu.cmu.cs.mvelezce.sleep.statements.TimedProgram;
import edu.cmu.cs.mvelezce.sleep.statements.TimedStatement;

/**
 * Created by mvelezce on 4/13/17.
 */
public interface TimedVisitor<T,U> extends Visitor<T,U> {

    U visitTimedStatement(TimedStatement timedStatement);

    U visitTimedProgram(TimedProgram timedProgram);
}
