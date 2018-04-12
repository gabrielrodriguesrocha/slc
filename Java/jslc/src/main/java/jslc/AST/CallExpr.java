package jslc.AST;

import jslc.Lexer.Symbol;

import java.util.*;

public class CallExpr extends Expr {
	public CallExpr(Symbol id, ArrayList<Expr> exprList) {
		this.id = id;
		this.exprList = exprList;
	}

	public void genC () {

	}

	private Symbol id;
	private ArrayList <Expr> exprList;
}
