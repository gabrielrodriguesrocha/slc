package jslc.AST;

public class AssignStmt extends Stmt {
	public AssignStmt(AssignExpr e) {
		this.e = e;
	}

	public void genC(PW pw) {
		e.genC(pw);
		pw.out.println(";");
	}

	private AssignExpr e;
}
