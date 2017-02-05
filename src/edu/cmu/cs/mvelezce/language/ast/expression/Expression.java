package edu.cmu.cs.mvelezce.language.ast.expression;

import edu.cmu.cs.mvelezce.language.ast.Node;
import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;

/**
 * Created by miguelvelez on 1/31/17.
 */
public abstract class Expression extends Node {

    public abstract <T> T accept(Visitor<T> visitor);

}
