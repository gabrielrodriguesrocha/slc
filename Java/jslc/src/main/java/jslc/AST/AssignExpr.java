package jslc.AST;

import jslc.Lexer.Symbol;

public class AssignExpr extends Expr {
	public AssignExpr (Symbol id, Expr e) {
		this.id = id;
		this.e = e;
	}

	public void genC () {

	}

	private Symbol id;
	private Expr e;
}
