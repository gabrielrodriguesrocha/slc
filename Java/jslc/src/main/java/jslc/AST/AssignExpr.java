package jslc.AST;

import jslc.Lexer.Symbol;

public class AssignExpr extends Expr {
	public AssignExpr (String id, Expr e) {
		this.id = id;
		this.e = e;
	}

	public void genC (PW pw) {
		pw.println(id+" = ");
		e.genC(pw);
	}

	private String id;
	private Expr e;
}
