package jslc.AST;

public class AssignStmt {
	public AssignStmt(AssignExpr e) {
		this.e = e;
	}

	public void genC() {

	}

	private AssignExpr e;
}
