package jslc.AST;

public class CallStmt extends Stmt {
	
	public CallStmt(CallExpr c) {
		this.c = c;
	}

	public void genC (PW pw) {
		c.genC(pw, true);
		pw.out.println(";");
	}

	private CallExpr c;
}
