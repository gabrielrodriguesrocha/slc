package jslc.AST;

import java.util.*;

public class ForStmt extends Stmt {
	public ForStmt (AssignExpr init, CompExpr cond, AssignExpr step, ArrayList<Stmt> stmts) {
		this.init = init;
		this.cond = cond;
		this.step = step;
		this.stmts = stmts;
	}
	public void genC(PW pw){
		pw.println("for ( ");
		if(init != null){
			init.genC(pw);
		}
		pw.println("; ");
		if(cond != null){
			cond.genC(pw);
		}
		pw.println("; ");
		if(step != null){
			step.genC(pw);
		}
		pw.println(") {");

		for(Stmt a : stmts){
			a.genC(pw);
		}
		pw.println("}");

	}
	private AssignExpr init;
	private CompExpr cond;
	private AssignExpr step;
	private ArrayList<Stmt> stmts;
}
