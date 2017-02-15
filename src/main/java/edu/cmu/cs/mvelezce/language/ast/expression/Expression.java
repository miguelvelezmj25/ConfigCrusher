package edu.cmu.cs.mvelezce.language.ast.expression;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;
import edu.cmu.cs.mvelezce.language.ast.Node;

/**
 * An abstract expression in the AST.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public abstract class Expression extends Node {

    /**
     * Accept method of the Visitor Pattern that all Expressions that extend this class must implement.
     *
     * @param visitor
     * @param <T>
     * @param <U>
     * @return
     */
    public abstract <T,U> T accept(Visitor<T,U> visitor);
}
