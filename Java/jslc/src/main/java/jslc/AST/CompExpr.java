package jslc.AST;

import jslc.Lexer.Symbol;

public class CompExpr extends Expr  {
	public CompExpr (Expr l, Symbol compop, Expr r) {
		this.l = l;
		this.compop = compop;
		this.r = r;
	}

	public void genC (PW pw) {
		l.genC(pw);
		if (compop == Symbol.EQUAL)
			pw.out.print(" == ");
		else
			pw.out.print(" " + compop.toString() + " ");
		r.genC(pw);

	}

	private Expr l;
	private Symbol compop;
	private Expr r;
}
