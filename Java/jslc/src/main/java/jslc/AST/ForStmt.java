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
		pw.print("for (");
		if(init != null){
			init.genC(pw, false);
		}
		pw.out.print("; ");
		if(cond != null){
			cond.genC(pw);
		}
		pw.out.print("; ");
		if(step != null){
			step.genC(pw, false);
		}
		pw.out.println(") {");
		pw.add();
		for(Stmt a : stmts){
			a.genC(pw);
		}
		pw.sub();
		pw.println("}");

	}
	public ArrayList<Stmt> getStmts(){
		return stmts;	
	}
	private AssignExpr init;
	private CompExpr cond;
	private AssignExpr step;
	private ArrayList<Stmt> stmts;
}
