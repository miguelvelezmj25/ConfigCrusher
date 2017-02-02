package edu.cmu.cs.mvelezce.interpreter;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.lexer.Lexer;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;
import edu.cmu.cs.mvelezce.interpreter.visitor.NodeVisitor;

import java.io.File;
import java.util.Scanner;

/**fix point loop
 * worklist algorithm from cfg
 * Transfer function is to check how abstraction is changed from statement to statement
 * Join function is to join abstractions after they have branched out
 * Created by miguelvelez on 1/31/17.
 */
public class SimpleInterpreter {

    public static void main(String[] args) throws Exception {
        String program = SimpleInterpreter.loadFile("src/edu/cmu/cs/mvelezce/interpreter/program1");
        System.out.println(program);
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        System.out.println(parser.parse());


    }

    public static String loadFile(String name) throws Exception {
        Scanner s = new Scanner(new File(name));
        String file = "";
        while (s.hasNext()) {
            file += s.nextLine() + "\n";
        }
        s.close();
        return file;
    }

}
