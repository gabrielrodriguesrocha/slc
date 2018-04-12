package jslc.AST;

import jslc.Lexer.Symbol;

public class AssignStmt extends Stmt {
	public AssignStmt(Symbol id, AssignExpr e) {
		this.id = id;
		this.e = e;
	}

	public void genC() {

	}

	private Symbol id;
	private AssignExpr e;
}
