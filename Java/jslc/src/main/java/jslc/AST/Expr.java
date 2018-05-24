package jslc.AST;

import java.util.*;

public class Expr implements Typeable {
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

	public Type getType () {
		t =  f.getType();
		if (t == Type.voidType) {
			return t;
		}
		for (ExprTail e : tail) {
			if (t != e.getType()) {
				if ((e.getType() == Type.intType && t == Type.floatType) ||
					(e.getType() == Type.floatType && t == Type.intType)) {
					t = Type.floatType;
				}
				else if (e.getType() == Type.voidType) {
					t = Type.voidType;
				}
			}
		}
		return t;
	}

	private Factor f;
	private ArrayList<ExprTail> tail;
	private Type t;
}	
