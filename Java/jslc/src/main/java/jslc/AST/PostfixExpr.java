package jslc.AST;

import jslc.Lexer.Symbol;

public class PostfixExpr extends Expr {
	public PostfixExpr () {}

	public PostfixExpr (Expr e) {
		this.e = e;
	}

	public void genC () {
			
	}

	private Expr e;
}
