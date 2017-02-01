package edu.cmu.cs.mvelezce.interpreter;

import edu.cmu.cs.mvelezce.interpreter.lexer.Lexer;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class SimpleInterpreter {

    public static void main(String[] args) {
        Lexer lexer = new Lexer("2+3546");
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());

        lexer = new Lexer("2+3546");
        Parser parser = new Parser(lexer);
        System.out.println(parser.parse());
    }

}
