package jslc.AST;

import java.util.*;

public class FuncBody {
	public FuncBody (ArrayList<Decl> decls, ArrayList<Stmt> stmts) {
		this.decls = decls;
		this.stmts = stmts;
	}

	public void genC(PW pw) {
		for(Decl a: decls){
			a.genC(pw);
		}
		for(Stmt b: stmts){
			b.genC(pw);
		}
	}

	private Decl decls;
	private ArrayList<Stmt> stmts;
}	
