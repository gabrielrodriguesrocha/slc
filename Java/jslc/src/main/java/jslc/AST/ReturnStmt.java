package jslc.AST;

public class ReturnStmt extends Stmt {
	public ReturnStmt (Expr e) {
		this.e = e;
	}

	void genC () {

	}

	private Expr e;
}
