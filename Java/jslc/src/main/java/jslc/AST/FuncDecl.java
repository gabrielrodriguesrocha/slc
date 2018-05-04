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
		itr = params.iterator();
		if(itr.hasNext()){
			itr = params.iterator();
			itr.next().genC(pw);

			while(itr.hasNext()) {
				itr.next().genC(pw);
				pw.out.print(", ");
			}
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
	private Iterator<Param> itr;
}
