package jslc.AST;

import java.util.*;

public class IfStmt extends Stmt {
	public IfStmt (CompExpr cond, ArrayList<Stmt> stmts, ElseStmt elsePart) { // Com Else
		this.cond = cond;
		this.stmts = stmts;
		this.elsePart = elsePart; // Construir como null se não tiver else, verificar no genC
	}

	public void genC(PW pw) {
		pw.print("if (");
		cond.genC(pw);
		pw.out.println(") {");
		pw.add();
		for(Stmt a : stmts){
			a.genC(pw);
		}
		pw.sub();
		pw.println("}");
		if(elsePart != null){
			elsePart.genC(pw);
		}
	}

	private CompExpr cond;
	private ArrayList<Stmt> stmts;
	private ElseStmt elsePart;
}
