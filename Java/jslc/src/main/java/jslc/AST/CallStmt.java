package jslc.AST;

public class CallStmt {
	
	public CallStmt(CallExpr c) {
		this.c = c;
	}

	public void genC () {

	}

	private CallExpr c;
}
