package jslc.AST;

import java.util.*;

public class Factor extends Expr {
	public Factor (PostfixExpr p, ArrayList <FactorList> tail) {
		this.p = p;
		this.tail = tail;
	}
	
	public void genC () {
	
	}

	private PostfixExpr p;
	private ArrayList <FactorList> tail;
}	
