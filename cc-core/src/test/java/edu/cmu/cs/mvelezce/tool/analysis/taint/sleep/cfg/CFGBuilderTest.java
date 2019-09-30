package edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg;

/**
 * Created by mvelezce on 2/6/17.
 */
public class CFGBuilderTest {

//    @Test
//    public void test1() {
//        String programFile = Incling.loadFile(Incling.PROGRAMS_PATH + "program1");
//        Lexer lexer = new Lexer(programFile);
//        Parser parser = new Parser(lexer);
//        Program program = parser.parse();
//        CFGBuilder build = new CFGBuilder();
//
//        int steps = 0;
//        CFG cfg = new CFG();
//        Stack<BasicBlock> branchStack = new Stack<>();
//
//        Statement statement = new StatementAssignment(new ExpressionVariable("a"), new ExpressionConfigurationConstant("A"));
//        BasicBlock basicBlock = new BasicBlock(statement);
//        cfg.addEdge(cfg.getEntry(), basicBlock);
//        BasicBlock currentBasicBlock = basicBlock;
//
//        statement = new StatementAssignment(new ExpressionVariable("b"), new ExpressionConfigurationConstant("B"));
//        basicBlock = new BasicBlock(statement);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//
//        statement = new StatementSleep(new ExpressionVariable("a"));
//        basicBlock = new BasicBlock(statement);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//
//        List<Statement> statements = new LinkedList<>();
//        statements.add(new StatementSleep(new ExpressionConstantInt(2)));
//
//        StatementIf statementIf = new StatementIf(new ExpressionBinary(
//                new ExpressionVariable("a"), "&&", new ExpressionVariable("b")), new StatementBlock(statements));
//        basicBlock = new BasicBlock(statementIf);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//        branchStack.push(currentBasicBlock);
//
//        List<Expression> conditions = new ArrayList<>();
//        conditions.add(new ExpressionVariable("a"));
//        conditions.add(new ExpressionVariable("b"));
//        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
//            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement, conditions);
//            cfg.addEdge(currentBasicBlock, basicBlock);
//            currentBasicBlock = basicBlock;
//        }
//
//        statement = new StatementSleep(new ExpressionConstantInt(3));
//        basicBlock = new BasicBlock(statement);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//
//        cfg.addEdge(branchStack.pop(), currentBasicBlock);
//        cfg.addEdge(currentBasicBlock, cfg.getExit());
//
//        CFG builtCFG = build.buildCFG(program);
//
//        BasicBlock currentBasicBlockCFG = cfg.getSuccessors(cfg.getEntry()).get(0);
//        BasicBlock currentBasicBlockBuiltCFG = builtCFG.getSuccessors(builtCFG.getEntry()).get(0);
//        boolean equals = true;
//
//        while(!currentBasicBlockCFG.myEquals(cfg.getExit()) || !currentBasicBlockBuiltCFG.myEquals(builtCFG.getExit())) {
//            if(!currentBasicBlockCFG.myEquals(currentBasicBlockBuiltCFG)) {
//                equals = false;
//                break;
//            }
//
//            currentBasicBlockCFG = cfg.getSuccessors(currentBasicBlock).get(0);
//            currentBasicBlockBuiltCFG = builtCFG.getSuccessors(currentBasicBlockBuiltCFG).get(0);
//        }
//
//        Assert.assertTrue(equals);
//
//    }
//
//    @Test
//    public void test2() {
//        String programFile = Incling.loadFile(Incling.PROGRAMS_PATH + "program2");
//        Lexer lexer = new Lexer(programFile);
//        Parser parser = new Parser(lexer);
//        Program program = parser.parse();
//        CFGBuilder build = new CFGBuilder();
//
//        int steps = 0;
//        CFG cfg = new CFG();
//        Stack<BasicBlock> branchStack = new Stack<>();
//        boolean cameFromInner = false;
//
//        Statement statement = new StatementAssignment(new ExpressionVariable("a"), new ExpressionConfigurationConstant("A"));
//        BasicBlock basicBlock = new BasicBlock(statement);
//        cfg.addEdge(cfg.getEntry(), basicBlock);
//        BasicBlock currentBasicBlock = basicBlock;
//
//        List<Statement> statementsInner = new LinkedList<>();
//        statementsInner.add(new StatementSleep(new ExpressionConstantInt(2)));
//
//        List<Statement> statements = new LinkedList<>();
//        statements.add(new StatementSleep(new ExpressionVariable("a")));
//        statements.add(new StatementAssignment(new ExpressionVariable("b"), new ExpressionConfigurationConstant("B")));
//        statements.add(new StatementIf(new ExpressionUnary("!", new ExpressionVariable("b")), new StatementBlock(statementsInner)));
//        statements.add(new StatementSleep(new ExpressionConstantInt(3)));
//
//        StatementIf statementIf = new StatementIf(new ExpressionVariable("a"), new StatementBlock(statements));
//        basicBlock = new BasicBlock(statementIf);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//        branchStack.push(currentBasicBlock);
//
//        List<Expression> conditions = new ArrayList<>();
//        conditions.add(new ExpressionVariable("a"));
//        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
//            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement, conditions);
//            cfg.addEdge(currentBasicBlock, basicBlock);
//            currentBasicBlock = basicBlock;
//
//            if(cameFromInner) {
//                cfg.addEdge(branchStack.pop(), currentBasicBlock);
//                cameFromInner = false;
//            }
//
//            if(trueStatement instanceof StatementIf) {
//                cameFromInner = true;
//                branchStack.push(currentBasicBlock);
//
//                List<Expression> innerConditions = new ArrayList<>(conditions);
//                innerConditions.add(new ExpressionVariable("b"));
//                for(Statement trueStatementInner : ((StatementBlock) ((StatementIf) trueStatement).getThenBlock()).getStatements()) {
//                    basicBlock = new BasicBlock(steps++ + "| " + trueStatementInner, trueStatementInner, innerConditions);
//                    cfg.addEdge(currentBasicBlock, basicBlock);
//                    currentBasicBlock = basicBlock;
//                }
//
//            }
//        }
//
//        cfg.addEdge(branchStack.pop(), cfg.getExit());
//        cfg.addEdge(currentBasicBlock, cfg.getExit());
//
//        Assert.assertEquals(cfg, build.buildCFG(program));
//    }
//
//    @Test
//    public void test3() {
//        String programFile = Incling.loadFile(Incling.PROGRAMS_PATH + "program3");
//        Lexer lexer = new Lexer(programFile);
//        Parser parser = new Parser(lexer);
//        Program program = parser.parse();
//        CFGBuilder build = new CFGBuilder();
//
//        int steps = 0;
//        CFG cfg = new CFG();
//        Stack<BasicBlock> branchStack = new Stack<>();
//
//        Statement statement = new StatementAssignment(new ExpressionVariable("a"), new ExpressionConfigurationConstant("A"));
//        BasicBlock basicBlock = new BasicBlock(statement);
//        cfg.addEdge(cfg.getEntry(), basicBlock);
//        BasicBlock currentBasicBlock = basicBlock;
//
//        statement = new StatementAssignment(new ExpressionVariable("b"), new ExpressionConfigurationConstant("B"));
//        basicBlock = new BasicBlock(statement);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//
//        statement = new StatementSleep(new ExpressionConstantInt(6));
//        basicBlock = new BasicBlock(statement);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//
//        List<Statement> statements = new LinkedList<>();
//        statements.add(new StatementSleep(new ExpressionConstantInt(3)));
//        statements.add(new StatementAssignment(new ExpressionVariable("b"), new ExpressionConstantInt(1)));
//
//        StatementIf statementIf = new StatementIf(new ExpressionVariable("a"), new StatementBlock(statements));
//        basicBlock = new BasicBlock(statementIf);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//        branchStack.push(currentBasicBlock);
//
//        List<Expression> conditions = new ArrayList<>();
//        conditions.add(statementIf.getCondition());
//        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
//            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement, conditions);
//            cfg.addEdge(currentBasicBlock, basicBlock);
//            currentBasicBlock = basicBlock;
//        }
//
//        statements = new LinkedList<>();
//        statements.add(new StatementSleep(new ExpressionConstantInt(1)));
//
//        statementIf = new StatementIf(new ExpressionVariable("b"), new StatementBlock(statements));
//        basicBlock = new BasicBlock(statementIf);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//        cfg.addEdge(branchStack.pop(), currentBasicBlock);
//        branchStack.push(currentBasicBlock);
//
//        conditions = new ArrayList<>();
//        conditions.add(statementIf.getCondition());
//        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
//            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement, conditions);
//            cfg.addEdge(currentBasicBlock, basicBlock);
//            currentBasicBlock = basicBlock;
//        }
//
//        cfg.addEdge(branchStack.pop(), cfg.getExit());
//        cfg.addEdge(currentBasicBlock, cfg.getExit());
//
//        Assert.assertEquals(cfg, build.buildCFG(program));
//    }
//
//    @Test
//    public void test4() {
//        String programFile = Incling.loadFile(Incling.PROGRAMS_PATH + "program4");
//        Lexer lexer = new Lexer(programFile);
//        Parser parser = new Parser(lexer);
//        Program program = parser.parse();
//        CFGBuilder build = new CFGBuilder();
//
//        int steps = 0;
//        CFG cfg = new CFG();
//        Stack<BasicBlock> branchStack = new Stack<>();
//
//        Statement statement = new StatementAssignment(new ExpressionVariable("a"), new ExpressionConfigurationConstant("A"));
//        BasicBlock basicBlock = new BasicBlock(statement);
//        cfg.addEdge(cfg.getEntry(), basicBlock);
//        BasicBlock currentBasicBlock = basicBlock;
//
//        statement = new StatementAssignment(new ExpressionVariable("b"), new ExpressionConfigurationConstant("B"));
//        basicBlock = new BasicBlock(statement);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//
//        statement = new StatementSleep(new ExpressionConstantInt(6));
//        basicBlock = new BasicBlock(statement);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//
//        List<Statement> statements = new LinkedList<>();
//        statements.add(new StatementSleep(new ExpressionConstantInt(3)));
//
//        StatementIf statementIf = new StatementIf(new ExpressionVariable("a"), new StatementBlock(statements));
//        basicBlock = new BasicBlock(statementIf);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//        branchStack.push(currentBasicBlock);
//
//        List<Expression> conditions = new ArrayList<>();
//        conditions.add(statementIf.getCondition());
//        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
//            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement, conditions);
//            cfg.addEdge(currentBasicBlock, basicBlock);
//            currentBasicBlock = basicBlock;
//        }
//
//        statements = new LinkedList<>();
//        statements.add(new StatementSleep(new ExpressionConstantInt(1)));
//
//        statementIf = new StatementIf(new ExpressionVariable("b"), new StatementBlock(statements));
//        basicBlock = new BasicBlock(statementIf);
//        cfg.addEdge(currentBasicBlock, basicBlock);
//        currentBasicBlock = basicBlock;
//        cfg.addEdge(branchStack.pop(), currentBasicBlock);
//        branchStack.push(currentBasicBlock);
//
//        conditions = new ArrayList<>();
//        conditions.add(statementIf.getCondition());
//        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
//            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement, conditions);
//            cfg.addEdge(currentBasicBlock, basicBlock);
//            currentBasicBlock = basicBlock;
//        }
//
//        cfg.addEdge(branchStack.pop(), cfg.getExit());
//        cfg.addEdge(currentBasicBlock, cfg.getExit());
//
//        Assert.assertEquals(cfg, build.buildCFG(program));
//    }

}