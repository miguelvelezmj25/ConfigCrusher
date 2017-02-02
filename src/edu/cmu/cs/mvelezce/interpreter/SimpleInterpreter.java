package edu.cmu.cs.mvelezce.interpreter;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.lexer.Lexer;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;
import edu.cmu.cs.mvelezce.interpreter.visitor.NodeVisitor;

/**fix pont loop
 * worklist algorithm from cfg
 * Created by miguelvelez on 1/31/17.
 */
public class SimpleInterpreter {

    public static void main(String[] args) {
        Lexer lexer = new Lexer("a=2;\nb=a");
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());

    }

}
