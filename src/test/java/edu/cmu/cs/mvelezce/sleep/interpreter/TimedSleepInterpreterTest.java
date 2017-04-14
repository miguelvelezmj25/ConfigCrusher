package edu.cmu.cs.mvelezce.sleep.interpreter;

import edu.cmu.cs.mvelezce.sleep.Helper;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.ast.value.IntValue;
import edu.cmu.cs.mvelezce.sleep.lexer.Lexer;
import edu.cmu.cs.mvelezce.sleep.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 2/1/17.
 */
public class TimedSleepInterpreterTest {

    public static final String PROGRAMS_PATH = "src/main/java/edu/cmu/cs/mvelezce/sleep/programs/";

    @Test
    public void test1() throws Exception {
        String program = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program1");

        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(1));
        store.put("b", new IntValue(1));

        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("A");
        activatedConfigurations.add("B");

        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(ast);
        interpreter.evaluate(activatedConfigurations);

        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(6, interpreter.getTotalExecutionTime());
    }

//    @Test
//    public void test2() throws Exception {
//        String program = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program2");
//
//        Map<String, IntValue> store = new HashMap<>();
//        store.put("a", new IntValue(1));
//        store.put("b", new IntValue(0));
//
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        Set<String> activatedConfigurations = new HashSet<>();
//        activatedConfigurations.add("A");
//
//        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(ast);
//
//        interpreter.evaluate(activatedConfigurations);
//
//        Assert.assertEquals(store, interpreter.getStore());
//        Assert.assertEquals(6, interpreter.getTotalExecutionTime());
//    }
//
//    @Test
//    public void test3() throws Exception {
//        String program = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program3");
//
//        Map<String, IntValue> store = new HashMap<>();
//        store.put("a", new IntValue(1));
//        store.put("b", new IntValue(1));
//
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        Set<String> activatedConfigurations = new HashSet<>();
//        activatedConfigurations.add("A");
//        activatedConfigurations.add("B");
//
//        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(ast);
//
//        interpreter.evaluate(activatedConfigurations);
//
//        Assert.assertEquals(store, interpreter.getStore());
//        Assert.assertEquals(10, interpreter.getTotalExecutionTime());
//    }
//
//    @Test
//    public void test4() throws Exception {
//        String program = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program4");
//
//        Map<String, IntValue> store = new HashMap<>();
//        store.put("a", new IntValue(1));
//        store.put("b", new IntValue(1));
//
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        Set<String> activatedConfigurations = new HashSet<>();
//        activatedConfigurations.add("A");
//        activatedConfigurations.add("B");
//
//        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(ast);
//
//        interpreter.evaluate(activatedConfigurations);
//
//        Assert.assertEquals(store, interpreter.getStore());
//        Assert.assertEquals(10, interpreter.getTotalExecutionTime());
//    }
//
//    @Test
//    public void test5() throws Exception {
//        String program = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program5");
//
//        Map<String, IntValue> store = new HashMap<>();
//        store.put("a", new IntValue(1));
//        store.put("b", new IntValue(1));
//
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        Set<String> activatedConfigurations = new HashSet<>();
//        activatedConfigurations.add("A");
//        activatedConfigurations.add("B");
//
//        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(ast);
//
//        interpreter.evaluate(activatedConfigurations);
//
//        Assert.assertEquals(store, interpreter.getStore());
//        Assert.assertEquals(4, interpreter.getTotalExecutionTime());
//    }
//
//    @Test
//    public void test6() throws Exception {
//        String program = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program6");
//
//        Map<String, IntValue> store = new HashMap<>();
//        store.put("a", new IntValue(1));
//        store.put("b", new IntValue(1));
//        store.put("c", new IntValue(1));
//        store.put("x", new IntValue(6));
//
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        Set<String> activatedConfigurations = new HashSet<>();
//        activatedConfigurations.add("A");
//        activatedConfigurations.add("B");
//        activatedConfigurations.add("C");
//
//        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(ast);
//
//        interpreter.evaluate(activatedConfigurations);
//
//        Assert.assertEquals(store, interpreter.getStore());
//        Assert.assertEquals(6, interpreter.getTotalExecutionTime());
//    }

}