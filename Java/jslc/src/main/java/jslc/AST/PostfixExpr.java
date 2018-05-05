package jslc.AST;

public class PostfixExpr extends Factor implements Typeable {
	public PostfixExpr () {}

	public PostfixExpr (Expr e) {
		this.e = e;
	}

	public void genC (PW pw) {
		pw.out.print("(");
		e.genC(pw);
		pw.out.print(")");
	}

	public Type getType() {
		return e.getType();
	}

	private Expr e;
}
