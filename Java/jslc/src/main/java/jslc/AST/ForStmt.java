package jslc.AST;

import java.util.*;

public class ForStmt {
	public ForStmt (AssignExpr init, CompExpr cond, AssignExpr step, ArrayList<Stmt> stmts) {
		this.init = init;
		this.cond = cond;
		this.step = step;
		this.stmts = stmts;
	}

	private AssignExpr init;
	private CompExpr cond;
	private AssignExpr step;
	private ArrayList<Stmt> stmts;
}
