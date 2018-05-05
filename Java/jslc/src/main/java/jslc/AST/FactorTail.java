package jslc.AST;

import jslc.Lexer.Symbol;

public class FactorTail implements Typeable {
	public FactorTail (Symbol op, PostfixExpr p) {
		this.op = op;
		this.p = p;
	}

	public void genC (PW pw) {
		pw.out.print(" " + op.toString() + " ");
		p.genC(pw);
	}

	public Type getType() {
		return p.getType();
	}

	private Symbol op;
	private PostfixExpr p;
}
