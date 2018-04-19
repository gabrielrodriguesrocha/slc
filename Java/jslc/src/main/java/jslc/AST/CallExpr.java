package jslc.AST;

import java.util.*;

public class CallExpr extends PostfixExpr {
	public CallExpr(String id, ArrayList<Expr> exprList) {
		this.id = id;
		this.exprList = exprList;
	}

	public void genC (PW pw) {
		pw.print(id+"(");
		if(exprList != null){
			for (Expr e : exprList) {
				e.genC(pw);
			}
		}
		pw.out.print(")");
	}

	private String id;
	private ArrayList <Expr> exprList;
}
