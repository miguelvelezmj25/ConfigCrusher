package edu.cmu.cs.mvelezce.interpreter;

import edu.cmu.cs.mvelezce.interpreter.ast.statement.Statement;
import edu.cmu.cs.mvelezce.interpreter.ast.value.ValueInt;
import edu.cmu.cs.mvelezce.interpreter.lexer.Lexer;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;
import edu.cmu.cs.mvelezce.interpreter.visitor.NodeVisitor;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mvelezce on 2/1/17.
 */
public class SimpleInterpreterTest {
    private static final String PATH = "src/edu/cmu/cs/mvelezce/interpreter/programs/";

    @Test
    public void test1() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program1");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(1));
        store.put("C", new ValueInt(1));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(store, interpreter.evaluate(ast));
    }

    @Test
    public void test2() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program2");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(1));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(store, interpreter.evaluate(ast));
    }

    @Test
    public void test3() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program3");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(0));
        store.put("C", new ValueInt(0));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(store, interpreter.evaluate(ast));
    }

    @Test
    public void test4() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program4");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(0));
        store.put("b", new ValueInt(0));
        store.put("C", new ValueInt(0));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(store, interpreter.evaluate(ast));
    }

    @Test
    public void test5() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program5");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(1));
        store.put("C", new ValueInt(0));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(store, interpreter.evaluate(ast));
    }
//
//    @Test
//    public void test6() {
//        Lexer lexer = new Lexer(" 1 + 2 * 5 - 6 / 2   ");
//        Parser parser = new Parser(lexer);
//        Expression ast = parser.parse();
//        NodeVisitor interpreter = new NodeVisitor(parser);
//        Assert.assertEquals(8, interpreter.evaluate(ast).getValue());
//    }
//
//    @Test
//    public void test7() {
//        Lexer lexer = new Lexer(" (2 + 7) * 4   ");
//        Parser parser = new Parser(lexer);
//        Expression ast = parser.parse();
//        NodeVisitor interpreter = new NodeVisitor(parser);
//        Assert.assertEquals(36, interpreter.evaluate(ast).getValue());
//    }
//
//    @Test
//    public void test8() {
//        Lexer lexer = new Lexer("2 * ((3 + 14) + 6 / 2) ");
//        Parser parser = new Parser(lexer);
//        Expression ast = parser.parse();
//        NodeVisitor interpreter = new NodeVisitor(parser);
//        Assert.assertEquals(40, interpreter.evaluate(ast).getValue());
//    }
//
//    @Test
//    public void test9() {
//        Lexer lexer = new Lexer("7 + 3 * (10 / (12 / (3 + 1) - 1))   ");
//        Parser parser = new Parser(lexer);
//        Expression ast = parser.parse();
//        NodeVisitor interpreter = new NodeVisitor(parser);
//        Assert.assertEquals(22, interpreter.evaluate(ast).getValue());
//    }
//
//    @Test
//    public void test10() {
//        Lexer lexer = new Lexer(" 5--2   ");
//        Parser parser = new Parser(lexer);
//        Expression ast = parser.parse();
//        NodeVisitor interpreter = new NodeVisitor(parser);
//        Assert.assertEquals(7, interpreter.evaluate(ast).getValue());
//    }
//
//    @Test
//    public void test11() {
//        Lexer lexer = new Lexer(" 5 - - - + - (3 + 4) - +2 ");
//        Parser parser = new Parser(lexer);
//        Expression ast = parser.parse();
//        NodeVisitor interpreter = new NodeVisitor(parser);
//        Assert.assertEquals(10, interpreter.evaluate(ast).getValue());
//    }
//
//    @Test
//    public void test12() {
//        Lexer lexer = new Lexer(" a = 5 ");
//        Parser parser = new Parser(lexer);
//        Expression ast = parser.parse();
//        NodeVisitor interpreter = new NodeVisitor(parser);
//        Assert.assertEquals(5, interpreter.evaluate(ast).getValue());
//    }

}