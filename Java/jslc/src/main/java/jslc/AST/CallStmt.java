package jslc.AST;

public class CallStmt extends Stmt {
	
	public CallStmt(CallExpr c) {
		this.c = c;
	}

	public void genC () {

	}

	private CallExpr c;
}
