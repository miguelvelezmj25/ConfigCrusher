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

    @Test
    public void test6() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program6");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(1));
        store.put("C", new ValueInt(0));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        NodeVisitor interpreter = new NodeVisitor(parser);
        Assert.assertEquals(store, interpreter.evaluate(ast));
    }

}