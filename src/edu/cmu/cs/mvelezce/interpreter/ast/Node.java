package edu.cmu.cs.mvelezce.interpreter.ast;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by miguelvelez on 1/31/17.
 */
public abstract class Node {

    public abstract <T> T accept(Visitor<T> visitor);

}
