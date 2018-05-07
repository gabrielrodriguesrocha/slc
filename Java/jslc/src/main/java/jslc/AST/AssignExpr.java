package jslc.AST;

public class AssignExpr extends Expr {
	public AssignExpr (String id, Expr e) {
		this.id = id;
		this.e = e;
	}

	public void genC (PW pw, boolean indent) {
		if(indent) {
			pw.print(id+" = ");
		} else {
			pw.out.print(id+" = ");
		}
		e.genC(pw);
	}

	private String id;
	private Expr e;
}
