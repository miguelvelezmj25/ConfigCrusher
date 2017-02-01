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
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());

        lexer = new Lexer(" 3  + 5 *   8 - 12   ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        System.out.println(ast);
        NodeVisitor interpreter = new NodeVisitor(parser);
        System.out.println(ast);
        System.out.println("31 == " +interpreter.evaluate(ast));
        System.out.println(ast);
        System.out.println("");

        lexer = new Lexer("   2+3  ");
        parser = new Parser(lexer);
        ast = parser.parse();
        interpreter = new NodeVisitor(parser);
        System.out.println("5 == " + interpreter.evaluate(ast));

        lexer = new Lexer("2 + 7 * 4");
        parser = new Parser(lexer);
        ast = parser.parse();
        interpreter = new NodeVisitor(parser);
        System.out.println("30 == " + interpreter.evaluate(ast));

        lexer = new Lexer("7 - 8 / 4");
        parser = new Parser(lexer);
        ast = parser.parse();
        interpreter = new NodeVisitor(parser);
        System.out.println("5 == " + interpreter.evaluate(ast));

        lexer = new Lexer("14 + 2 * 3 - 6 / 2");
        parser = new Parser(lexer);
        ast = parser.parse();
        interpreter = new NodeVisitor(parser);
        System.out.println("17 == " +interpreter.evaluate(ast));

        lexer = new Lexer("1 + 2 * 5 - 6 / 2");
        parser = new Parser(lexer);
        ast = parser.parse();
        interpreter = new NodeVisitor(parser);
        System.out.println("8 == " + interpreter.evaluate(ast));

        lexer = new Lexer("(2 + 7) * 4");
        parser = new Parser(lexer);
        ast = parser.parse();
        interpreter = new NodeVisitor(parser);
        System.out.println("36 == " +interpreter.evaluate(ast));

        lexer = new Lexer("2 * ((3 + 14) + 6 / 2)");
        parser = new Parser(lexer);
        ast = parser.parse();
        interpreter = new NodeVisitor(parser);
        System.out.println("40 == " +interpreter.evaluate(ast));

        lexer = new Lexer("7 + 3 * (10 / (12 / (3 + 1) - 1))");
        parser = new Parser(lexer);
        ast = parser.parse();
        interpreter = new NodeVisitor(parser);
        System.out.println("22 == " +interpreter.evaluate(ast));

        lexer = new Lexer(" 5--2");
        parser = new Parser(lexer);
        ast = parser.parse();
        interpreter = new NodeVisitor(parser);
        System.out.println("7 == " +interpreter.evaluate(ast));

        lexer = new Lexer("  5 - - - + - (3 + 4) - +2");
        parser = new Parser(lexer);
        ast = parser.parse();
        interpreter = new NodeVisitor(parser);
        System.out.println("10 == " +interpreter.evaluate(ast));
    }

}
