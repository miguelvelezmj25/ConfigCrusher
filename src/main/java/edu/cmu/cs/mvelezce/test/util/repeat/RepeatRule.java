package edu.cmu.cs.mvelezce.test.util.repeat;

/**
 * Created by mvelezce on 6/30/17.
 */

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RepeatRule implements TestRule {

    private RepeatStatement repeatStatement;

    public int getIteration() { return repeatStatement.iteration; }

    private static class RepeatStatement extends Statement {

        private int iteration = 0;
        private final int times;
        private final Statement statement;

        private RepeatStatement(int times, Statement statement) {
            this.times = times;
            this.statement = statement;
        }

        @Override
        public void evaluate() throws Throwable {
            for(this.iteration = 0; iteration < times; this.iteration++) {
                statement.evaluate();
            }
        }
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        Statement result = statement;
        Repeat repeat = description.getAnnotation(Repeat.class);
        if(repeat != null) {
            int times = repeat.times();
            result = new RepeatStatement(times, statement);
        }

        this.repeatStatement = (RepeatStatement) result;
        return result;
    }
}
