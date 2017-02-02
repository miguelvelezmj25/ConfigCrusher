package edu.cmu.cs.mvelezce.interpreter;

import edu.cmu.cs.mvelezce.interpreter.lexer.Lexer;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mvelezce on 2/1/17.
 */
public class ParserTest {
    private static final String PATH = "src/edu/cmu/cs/mvelezce/interpreter/programs/";

    @Test
    public void test1() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program1");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Assert.assertEquals(program, parser.parse());
    }

    @Test
    public void test2() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program2");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Assert.assertEquals(program, parser.parse());
    }

    @Test
    public void test3() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program3");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Assert.assertEquals(program, parser.parse());
    }

    @Test
    public void test4() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program4");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Assert.assertEquals(program, parser.parse());
    }

    @Test
    public void test5() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program5");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Assert.assertEquals(program, parser.parse());
    }

}