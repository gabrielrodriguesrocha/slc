package jslc.AST;

import jslc.Lexer.Symbol;

public class ExprTail {
	public ExprTail (Symbol op, Factor f) {
		this.op = op;
		this.f = f;
	}

	public void genC (PW pw) {
		pw.print(op.toString() + " ");
		f.genC(pw);
	}

	private Symbol op;
	private Factor f;
}
