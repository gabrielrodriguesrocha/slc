package jslc.AST;

import java.util.*;

public class CallExpr extends PostfixExpr {
	public CallExpr(String id, ArrayList<Expr> exprList) {
		this.id = id;
		this.exprList = exprList;
	}

	public void genC (PW pw) {
		pw.out.print(id+"(");
		if(exprList != null){
			itr = exprList.iterator();
			itr.next().genC(pw);

			while(itr.hasNext()) {
				itr.next().genC(pw);
				pw.out.print(", ");
			}
		}
		pw.out.print(")");
	}

	public void genC (PW pw, boolean indent) {
		pw.print(id+"(");
		if(exprList != null){
			itr = exprList.iterator();
			itr.next().genC(pw);

			while(itr.hasNext()) {
				itr.next().genC(pw);
				pw.out.print(", ");
			}
		}
		pw.out.print(")");
	}

	private String id;
	private ArrayList <Expr> exprList;
	private Iterator <Expr> itr;
}
