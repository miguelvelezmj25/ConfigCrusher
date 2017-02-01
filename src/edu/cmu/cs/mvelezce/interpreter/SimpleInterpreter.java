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
        Lexer lexer = new Lexer(" 3  + 5 *   8 - 12   ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        System.out.println("31 == " +interpreter.evaluate(ast));
    }

}
