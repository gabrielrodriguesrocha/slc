package jslc.AST;

import java.util.*;

public class Factor extends Expr {
	public Factor () {}

	public Factor (PostfixExpr p, ArrayList<FactorTail> tail) {
		this.p = p;
		this.tail = tail;
	}
	
	public void genC (PW pw) {
		p.genC(pw);
		for (FactorTail f : tail)  {
			f.genC(pw);
		}
	}

	private PostfixExpr p;
	private ArrayList<FactorTail> tail;
}	
