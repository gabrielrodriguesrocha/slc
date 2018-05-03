package jslc.AST;

import java.util.*;

public class FuncDecl {
	public FuncDecl (Type type, 
					 String id, 
					 ArrayList<Param> params, 
					 ArrayList<Decl> decls,
					 ArrayList<Stmt> stmts) {
		this.type = type;
		this.id = id;
		this.params = params;
		this.decls = decls;
		this.stmts = stmts;
	}

	public void genC(PW pw) {
		pw.out.print(type.getCname() + " " + id + " (");
		int i = 0;
		for(Param a : params){
			if(i != 0){
				pw.out.print(",");
			}
			a.genC(pw);
			i = 1;
		}
		pw.out.println(") {");
		pw.add();
		for(Decl a: decls){
			a.genC(pw);
		}
		for(Stmt b: stmts){
			b.genC(pw);
		}
		pw.sub();
		pw.println("}\n");
		
	}

	private Type type;
	private String id;
	private ArrayList<Param> params;
	private ArrayList<Decl> decls;
	private ArrayList<Stmt> stmts;
}
