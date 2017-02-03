package edu.cmu.cs.mvelezce.interpreter.cfg;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.*;
import edu.cmu.cs.mvelezce.interpreter.ast.statement.*;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;
import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by miguelvelez on 2/2/17.
 */
public class CFGVisitor implements Visitor {

    private Parser parser;

    public CFGVisitor(Parser parser) {
        this.parser = parser;
    }

    public CFG buildCFG(Statement ast) {
        ast.accept(this);
        return null;
    }

    @Override
    public Object visitExpressionBinary(ExpressionBinary expressionBinary) {
        return null;
    }

    @Override
    public Object visitExpressionConfiguration(ExpressionConfiguration expressionConfiguration) {
        return null;
    }

    @Override
    public Object visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt) {
        return null;
    }

    @Override
    public Object visitExpressionUnary(ExpressionUnary expressionUnary) {
        return null;
    }

    @Override
    public Object visitExpressionVariable(ExpressionVariable varExpr) {
        return null;
    }

    @Override
    public void visitStatementAssignment(StatementAssignment statementAssignment) {

    }

    @Override
    public void visitStatementBlock(StatementBlock statementBlock) {

    }

    @Override
    public void visitStatementIf(StatementIf statementIf) {

    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {

    }

    @Override
    public void visitStatementWhile(StatementWhile statementAssignment) {

    }
}
