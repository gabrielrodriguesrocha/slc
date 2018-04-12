package jslc.AST;

import java.util.*;

public class IfStmt extends Stmt {
	public IfStmt (CompExpr cond, ArrayList<Stmt> stmts, ElseStmt elsePart) { // Com Else
		this.cond = cond;
		this.stmts = stmts;
		this.elsePart = elsePart; // Construir como null se não tiver else, verificar no genC
	}

	public void genC() {

	}

	private CompExpr cond;
	private ArrayList<Stmt> stmts;
	private elseStmt elsePart;
}
