package jslc.AST;

import jslc.Lexer.Symbol;

import java.util.*;

public class FuncDecl {
	public FuncDecl (Symbol type, String id, ArrayList<Param> params, FuncBody body) {
		this.type = type;
		this.params = params;
		this.body = body;
	}

	public void genC(PW pw) {
		pw.println(type.toString()+" ");
		pw.println(id+" ( ");
		for(Param a : params){
			a.genC(pw);
			pw.println(",");

		}
		pw.println("){");
		body.genC(pw);
		pw.println("}");
		
	}

	private Symbol type;
	private String id;
	private ArrayList<Param> params;
	private FuncBody body;
}
