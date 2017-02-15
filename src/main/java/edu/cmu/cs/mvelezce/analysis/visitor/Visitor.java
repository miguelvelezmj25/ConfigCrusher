package edu.cmu.cs.mvelezce.analysis.visitor;

import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

/**
 * An interface that specifies the methods of the Visitor Pattern.
 *
 * @param <V> the type of elements returned by the Visitor.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
interface Visitor <V> {

    /**
     * Evaluates an ExpressionBinary.
     *
     * @param expressionBinary
     * @return
     */
    V visitExpressionBinary(ExpressionBinary expressionBinary);

    /**
     * Evaluates an ExpressionConfigurationConstant.
     *
     * @param expressionConfigurationConstant
     * @return
     */
    V visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant);

    /**
     * Evaluates an ExpressionConstantInt.
     *
     * @param expressionConstantInt
     * @return
     */
    V visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt);

    /**
     * Evaluates an ExpressionUnary.
     *
     * @param expressionUnary
     * @return
     */
    V visitExpressionUnary(ExpressionUnary expressionUnary);

    /**
     * Evaluates an ExpressionVariable.
     *
     * @param expressionVariable
     * @return
     */
    V visitExpressionVariable(ExpressionVariable expressionVariable);

    /**
     * Visit a StatementAssignment.
     *
     * @param statementAssignment
     */
    void visitStatementAssignment(StatementAssignment statementAssignment);

    /**
     * Visit a StatementBlock.
     *
     * @param statementBlock
     */
    void visitStatementBlock(StatementBlock statementBlock);

    /**
     * Visit a StatementIf.
     *
     * @param statementIf
     */
    void visitStatementIf(StatementIf statementIf);

    /**
     * Visit a StatementSleep.
     *
     * @param statementSleep
     */
    void visitStatementSleep(StatementSleep statementSleep);

    /**
     * Visit a StatementWhile.
     *
     * @param statementWhile
     */
    void visitStatementWhile(StatementWhile statementWhile);
}
