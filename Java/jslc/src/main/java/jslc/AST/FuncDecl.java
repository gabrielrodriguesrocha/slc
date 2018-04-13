package jslc.AST;

import jslc.Lexer.Symbol;

import java.util.*;

public class FuncDecl {
	public FuncDecl (Symbol type, 
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
		pw.print(type.toString()+" ");
		pw.print(id + " (");
		for(Param a : params){
			a.genC(pw);
			pw.println(",");
		}
		pw.println(") {");
		for(Decl a: decls){
			a.genC(pw);
		}
		for(Stmt b: stmts){
			b.genC(pw);
		}
		pw.println("}");
		
	}

	private Symbol type;
	private String id;
	private ArrayList<Param> params;
	private ArrayList<Decl> decls;
	private ArrayList<Stmt> stmts;
}
