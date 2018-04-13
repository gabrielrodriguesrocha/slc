package jslc.AST;

import jslc.Lexer.Symbol;

public class FactorTail {
	public FactorTail (Symbol op, PostfixExpr p) {
		this.op = op;
		this.p = p;
	}

	public void genC (PW pw) {
		pw.print(" " + op.toString() + " ");
		p.genC(pw);
	}

	private Symbol op;
	private PostfixExpr p;
}
