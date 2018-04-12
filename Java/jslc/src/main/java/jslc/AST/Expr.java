package jslc.AST;

import java.util.*;

public class Expr {
	public Expr () {}
	
	public Expr (Factor f, ArrayList <ExprList> tail) {
		this.f = f;
		this.tail = tail;
	}
	
	public void genC () {
	
	}

	private Factor f;
	private ArrayList <ExprList> tail;
}	
