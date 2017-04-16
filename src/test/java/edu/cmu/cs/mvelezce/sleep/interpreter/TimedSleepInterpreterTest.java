package edu.cmu.cs.mvelezce.sleep.interpreter;

import edu.cmu.cs.mvelezce.sleep.Helper;
import edu.cmu.cs.mvelezce.sleep.ast.Program;
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
        // Compile
        String programFile = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program1");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        // Configurations
        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("A");
        activatedConfigurations.add("B");


        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(program);
        interpreter.evaluate(activatedConfigurations);

        // Store
        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(1));
        store.put("b", new IntValue(1));

        // Assert
        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(6, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test2() throws Exception {
        // Compile
        String programFile = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program2");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        // Configurations
        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("A");

        // Execute
        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(program);
        interpreter.evaluate(activatedConfigurations);

        // Store
        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(1));
        store.put("b", new IntValue(0));

        // Assert
        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(6, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test3() throws Exception {
        // Compile
        String programFile = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program3");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        // Configurations
        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("A");
        activatedConfigurations.add("B");

        // Execute
        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(program);
        interpreter.evaluate(activatedConfigurations);

        // Store
        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(1));
        store.put("b", new IntValue(1));

        // Assert
        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(10, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test4() throws Exception {
        // Compile
        String programFile = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program4");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        // Configurations
        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("A");
        activatedConfigurations.add("B");

        // Execute
        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(program);
        interpreter.evaluate(activatedConfigurations);

        // Store
        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(1));
        store.put("b", new IntValue(1));

        // Assert
        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(10, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test5() throws Exception {
        // Compile
        String programFile = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program5");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        // Configurations
        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("A");
        activatedConfigurations.add("B");

        // Execute
        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(program);
        interpreter.evaluate(activatedConfigurations);

        // Store
        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(1));
        store.put("b", new IntValue(1));

        // Assert
        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(4, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test6() throws Exception {
        // Compile
        String programFile = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program6");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        // Configurations
        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("A");
        activatedConfigurations.add("B");
        activatedConfigurations.add("C");

        // Execute
        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(program);
        interpreter.evaluate(activatedConfigurations);

        // Store
        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(1));
        store.put("b", new IntValue(1));
        store.put("c", new IntValue(1));
        store.put("x", new IntValue(6));

        // Assert
        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(6, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test7() throws Exception {
        // Compile
        String programFile = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program7");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        // Configurations
        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("A");
        activatedConfigurations.add("B");
        activatedConfigurations.add("D");

        // Execute
        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(program);
        interpreter.evaluate(activatedConfigurations);

        // Store
        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(1));
        store.put("b", new IntValue(1));
        store.put("c", new IntValue(0));
        store.put("d", new IntValue(1));

        // Assert
        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(3, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test8() throws Exception {
        // Compile
        String programFile = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program8");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        // Configurations
        Set<String> activatedConfigurations = new HashSet<>();

        // Execute
        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(program);
        interpreter.evaluate(activatedConfigurations);

        // Store
        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(0));
        store.put("b", new IntValue(0));
        store.put("c", new IntValue(0));

        // Assert
        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(0, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test9() throws Exception {
        // Compile
        String programFile = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program9");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        // Configurations
        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("A");

        // Execute
        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(program);
        interpreter.evaluate(activatedConfigurations);

        // Store
        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(2));

        // Assert
        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(3, interpreter.getTotalExecutionTime());
    }

    @Test
    public void test10() throws Exception {
        // Compile
        String programFile = Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program10");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        // Configurations
        Set<String> activatedConfigurations = new HashSet<>();
        activatedConfigurations.add("B");
        activatedConfigurations.add("C");
        activatedConfigurations.add("D");

        // Execute
        TimedSleepInterpreter interpreter = new TimedSleepInterpreter(program);
        interpreter.evaluate(activatedConfigurations);

        // Store
        Map<String, IntValue> store = new HashMap<>();
        store.put("a", new IntValue(0));
        store.put("b", new IntValue(1));
        store.put("c", new IntValue(1));
        store.put("d", new IntValue(1));

        // Assert
        Assert.assertEquals(store, interpreter.getStore());
        Assert.assertEquals(5, interpreter.getTotalExecutionTime());
    }
}