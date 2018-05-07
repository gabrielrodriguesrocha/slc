package jslc.AST;

public class ReturnStmt extends Stmt {
	public ReturnStmt (Expr e) {
		this.e = e;
	}

	public void genC (PW pw) {
		pw.print("return (");
		e.genC(pw);
		pw.out.println(");");
	}

	private Expr e;
}
