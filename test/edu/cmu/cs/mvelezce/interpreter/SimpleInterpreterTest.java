package edu.cmu.cs.mvelezce.interpreter;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.lexer.Lexer;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;
import edu.cmu.cs.mvelezce.interpreter.visitor.NodeVisitor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mvelezce on 2/1/17.
 */
public class SimpleInterpreterTest {

    @Test
    public void test1() {
        Lexer lexer = new Lexer(" 3  + 5 *   8 - 12   ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(31, interpreter.evaluate(ast).getValue());
    }

    @Test
    public void test2() {
        Lexer lexer = new Lexer("  2+3    ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(5, interpreter.evaluate(ast).getValue());
    }

    @Test
    public void test3() {
        Lexer lexer = new Lexer(" 2 + 7 * 4   ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(30, interpreter.evaluate(ast).getValue());
    }

    @Test
    public void test4() {
        Lexer lexer = new Lexer("7 - 8 / 4");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(5, interpreter.evaluate(ast).getValue());
    }

    @Test
    public void test5() {
        Lexer lexer = new Lexer(" 14 + 2 * 3 - 6 / 2   ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(17, interpreter.evaluate(ast).getValue());
    }

    @Test
    public void test6() {
        Lexer lexer = new Lexer(" 1 + 2 * 5 - 6 / 2   ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(8, interpreter.evaluate(ast).getValue());
    }

    @Test
    public void test7() {
        Lexer lexer = new Lexer(" (2 + 7) * 4   ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(36, interpreter.evaluate(ast).getValue());
    }

    @Test
    public void test8() {
        Lexer lexer = new Lexer("2 * ((3 + 14) + 6 / 2) ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(40, interpreter.evaluate(ast).getValue());
    }

    @Test
    public void test9() {
        Lexer lexer = new Lexer("7 + 3 * (10 / (12 / (3 + 1) - 1))   ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(22, interpreter.evaluate(ast).getValue());
    }

    @Test
    public void test10() {
        Lexer lexer = new Lexer(" 5--2   ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(7, interpreter.evaluate(ast).getValue());
    }

    @Test
    public void test11() {
        Lexer lexer = new Lexer(" 5 - - - + - (3 + 4) - +2 ");
        Parser parser = new Parser(lexer);
        Expression ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(10, interpreter.evaluate(ast).getValue());
    }

}