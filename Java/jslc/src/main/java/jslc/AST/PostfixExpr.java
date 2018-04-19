package jslc.AST;

public class PostfixExpr extends Factor {
	public PostfixExpr () {}

	public PostfixExpr (Expr e) {
		this.e = e;
	}

	public void genC (PW pw) {
		pw.out.print("(");
		e.genC(pw);
		pw.out.print(")");
	}

	private Expr e;
}
