package jslc.AST;

import jslc.Lexer.Symbol;

public class ExprTail implements Typeable {
	public ExprTail (Symbol op, Factor f) {
		this.op = op;
		this.f = f;
	}

	public void genC (PW pw) {
		pw.out.print(" " + op.toString() + " ");
		f.genC(pw);
	}

	public Type getType() {
		return f.getType();
	}

	private Symbol op;
	private Factor f;
}
