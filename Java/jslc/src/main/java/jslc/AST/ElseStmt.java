package jslc.AST;

public class ElseStmt {
	public ElseStmt (ArrayList <Stmt> stmts) {
		this.stmts = stmts;
	}

	public void genC () {

	}

	private ArrayList <Stmt> stmts;
}
