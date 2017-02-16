package edu.cmu.cs.mvelezce.analysis.interpreter;

import edu.cmu.cs.mvelezce.language.Helper;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;
import edu.cmu.cs.mvelezce.language.ast.value.ValueInt;
import edu.cmu.cs.mvelezce.language.lexer.Lexer;
import edu.cmu.cs.mvelezce.language.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 2/1/17.
 */
public class InterpreterTest {

    @Test
    public void test1() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program1");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(0));
        store.put("b", new ValueInt(0));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        Set<String> activatedConfigurations = new HashSet<>();

        Interpreter interpreter = new Interpreter();
        interpreter.evaluate(ast, activatedConfigurations);

        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(0, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test2() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program2");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(0));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("C");

        Interpreter interpreter = new Interpreter();

        interpreter.evaluate(ast, activatedConfigurations);

        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(1, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test3() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program3");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("b", new ValueInt(0));
        store.put("a", new ValueInt(1));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("C");

        Interpreter interpreter = new Interpreter();

        interpreter.evaluate(ast, activatedConfigurations);

        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(5, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test4() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program4");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(0));
        store.put("b", new ValueInt(2));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("C");

        Interpreter interpreter = new Interpreter();

        interpreter.evaluate(ast, activatedConfigurations);

        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(3, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test5() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program5");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(0));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("C");

        Interpreter interpreter = new Interpreter();

        interpreter.evaluate(ast, activatedConfigurations);

        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(2, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test6() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program6");

        Map<String, ValueInt> store = new HashMap<>();
        store.put("a", new ValueInt(1));
        store.put("b", new ValueInt(0));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("C");

        Interpreter interpreter = new Interpreter();

        interpreter.evaluate(ast, activatedConfigurations);

        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(6, interpreter.getTotalExecutionTime());
    }

}