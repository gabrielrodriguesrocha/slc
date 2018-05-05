package jslc.AST;

import java.util.*;

public class ElseStmt {
	public ElseStmt (ArrayList <Stmt> stmts) {
		this.stmts = stmts;
	}

	public void genC (PW pw) {
		pw.println("else {");
		pw.add();

		for(Stmt a: stmts){
			a.genC(pw);

		}
		pw.sub();
		pw.println("}");
	}
	public ArrayList<Stmt> getStmts(){
		return stmts;	
	}

	private ArrayList <Stmt> stmts;
}
