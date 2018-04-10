package jslc.AST;

import java.util.*;

public class FuncBody {
	public FuncBody (Decl decls, ArrayList<Stmt> stmts) {
		this.decls = decls;
		this.stmts = stmts;
	}

	public void genC() {
		
	}

	private Decl decls;
	private ArrayList<Stmt> stmts;
}	
