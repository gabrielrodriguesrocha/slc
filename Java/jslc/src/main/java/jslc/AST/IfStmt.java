package jslc.AST;

import java.util.*;

public class IfStmt extends Stmt {
	public IfStmt (CompExpr cond, ArrayList<Stmt> stmts, ElseStmt elsePart) { // Com Else
		this.cond = cond;
		this.stmts = stmts;
		this.elsePart = elsePart; // Construir como null se não tiver else, verificar no genC
	}

	public void genC(PW pw) {
		pw.println("if( ");
		cond.genC(pw);
		pw.println(") {");
		for(Stmt a : stmts){
			a.genC(pw);
		}
		pw.println("}");
		if(elsePart != NULL){
			elsePart.genC(pw);
		}
	}

	private CompExpr cond;
	private ArrayList<Stmt> stmts;
	private ElseStmt elsePart;
}
