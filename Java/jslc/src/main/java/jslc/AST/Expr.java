package jslc.AST;

import java.util.*;

public class Expr {
	public Expr () {}
	
	public Expr (Factor f, ArrayList<ExprTail> tail) {
		this.f = f;
		this.tail = tail;
	}
	
	public void genC (PW pw) {
		f.genC(pw);
		for (ExprTail e : tail) {
			e.genC(pw);
		}
	}

	private Factor f;
	private ArrayList<ExprTail> tail;
}	
