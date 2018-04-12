package jslc.AST;

import jslc.Lexer.Symbol;

import java.util.*;

public class CallExpr extends Expr {
	public CallExpr(String id, ArrayList<Expr> exprList) {
		this.id = id;
		this.exprList = exprList;
	}

<<<<<<< HEAD
	public void genC (PW pw) {
		pw.println(id+" ( ");
		if(exprList != null){
			for (Expr e : exprList) {
				e.genC(pw);
			}
		}
		pw.println(")");
=======
	public void genC () {
>>>>>>> 16370d54218b310fe640129154dc496c6eec59dc
		
	}

	private String id;
	private ArrayList <Expr> exprList;
}
