package jslc.AST;

public class PostfixExpr extends Expr {
	public PostfixExpr () {}

	public PostfixExpr (Expr e) {
		this.e = e;
	}

	public void genC (PW pw) {
		e.genC(pw);
	}

	private Expr e;
}
