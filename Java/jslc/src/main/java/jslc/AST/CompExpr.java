package jslc.AST;

import jslc.Lexer.Symbol;

public class CompExpr extends Expr  {
	public CompExpr (Expr l, Symbol compop, Expr r) {
		this.l = l;
		this.compop = compop;
		this.r = r;
	}

	public void genC () {

	}

	private Expr l;
	private Symbol compop;
	private Expr r;
}
