package edu.cmu.cs.mvelezce.language.ast.statement;

import edu.cmu.cs.mvelezce.language.ast.Node;
import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;

/**
 * Created by miguelvelez on 1/31/17.
 */
public abstract class Statement extends Node {


    public abstract <T> void accept(Visitor<T> visitor);

}
