package jslc.AST;

public class PostfixExpr extends Factor {
	public PostfixExpr () {}

	public PostfixExpr (Expr e) {
		this.e = e;
	}

	public void genC (PW pw) {
		pw.print("(");
		e.genC(pw);
		pw.print(")");
	}

	private Expr e;
}
