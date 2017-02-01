package edu.cmu.cs.mvelezce.interpreter;

import edu.cmu.cs.mvelezce.interpreter.lexer.Lexer;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class SimpleInterpreter {

    public static void main(String[] args) {
        Lexer lexer = new Lexer("2+3");
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
        System.out.println(lexer.getNextToken());
    }

}
