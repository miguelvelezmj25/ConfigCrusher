package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.interpreter.SimpleInterpreter;
import edu.cmu.cs.mvelezce.analysis.visitor.CFGVisitor;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;
import edu.cmu.cs.mvelezce.language.lexer.Lexer;
import edu.cmu.cs.mvelezce.language.parser.Parser;
import org.junit.Test;

/**
 * Created by miguelvelez on 2/5/17.
 */
public class TaintAnalysisTest {
    private static final String PATH = "src/edu/cmu/cs/mvelezce/language/programs/";

    @Test
    public void test1() throws Exception {
        String program = SimpleInterpreter.loadFile(PATH + "program1");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGVisitor builder = new CFGVisitor();
        CFG cfg = builder.buildCFG(ast);

        TaintAnalysis taintAnalysis = new TaintAnalysis(cfg);
        taintAnalysis.analyze();
    }

}