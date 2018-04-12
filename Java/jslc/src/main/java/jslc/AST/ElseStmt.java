package jslc.AST;

public class ElseStmt {
	public ElseStmt (ArrayList <Stmt> stmts) {
		this.stmts = stmts;
	}

	public void genC (PW pw) {
		pw.println("else {");

		for(Stmt a: stmts){
			a.genC(pw);

		}
		pw.println("}");
	}

	private ArrayList <Stmt> stmts;
}
