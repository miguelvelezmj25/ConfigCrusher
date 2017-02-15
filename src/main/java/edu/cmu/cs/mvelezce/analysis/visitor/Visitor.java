package edu.cmu.cs.mvelezce.analysis.visitor;

import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

/**
 * An interface that specifies the methods of the Visitor Pattern.
 *
 * @param <V> the type of elements returned by the Visitor.
 */
public interface Visitor <V> {

    /**
     * Evaluates an ExpressionBinary.
     *
     * @param expressionBinary
     * @return
     */
    public V visitExpressionBinary(ExpressionBinary expressionBinary);

    /**
     * Evaluates an ExpressionConfigurationConstant.
     *
     * @param expressionConfigurationConstant
     * @return
     */
    public V visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant);

    /**
     * Evaluates an ExpressionConstantInt.
     *
     * @param expressionConstantInt
     * @return
     */
    public V visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt);

    /**
     * Evaluates an ExpressionUnary.
     *
     * @param expressionUnary
     * @return
     */
    public V visitExpressionUnary(ExpressionUnary expressionUnary);

    /**
     * Evaluates an ExpressionVariable.
     *
     * @param expressionVariable
     * @return
     */
    public V visitExpressionVariable(ExpressionVariable expressionVariable);

    /**
     * Visit a StatementAssignment.
     *
     * @param statementAssignment
     */
    public void visitStatementAssignment(StatementAssignment statementAssignment);

    /**
     * Visit a StatementBlock.
     *
     * @param statementBlock
     */
    public void visitStatementBlock(StatementBlock statementBlock);

    /**
     * Visit a StatementIf.
     *
     * @param statementIf
     */
    public void visitStatementIf(StatementIf statementIf);

    /**
     * Visit a StatementSleep.
     *
     * @param statementSleep
     */
    public void visitStatementSleep(StatementSleep statementSleep);

    /**
     * Visit a StatementWhile.
     *
     * @param statementWhile
     */
    public void visitStatementWhile(StatementWhile statementWhile);
}
