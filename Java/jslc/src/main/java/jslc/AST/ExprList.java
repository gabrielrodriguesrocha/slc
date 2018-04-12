package jslc.AST;

import jslc.Lexer.Symbol;

public class ExprList {
	public ExprList (Symbol op, Expr e) {
		this.op = op;
		this.e = e;
	}

	public void genC () {

	}

	private Symbol op;
	private Expr e;
}
