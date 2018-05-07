package jslc.AST;

import java.util.*;

public class Factor extends Expr implements Typeable {
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

	public Type getType () {
		t = p.getType();
		if (t == Type.voidType) {
			return t;
		}
		for (FactorTail f : tail) {
			if (t != f.getType()) {
				if ((f.getType() == Type.intType && t == Type.floatType) ||
					(f.getType() == Type.intType && t == Type.floatType)) {
					t = Type.floatType;
				}
				else if (f.getType() == Type.voidType) {
					t = Type.voidType;
				}
			}
		}
		return t;
	}

	private PostfixExpr p;
	private ArrayList<FactorTail> tail;
	private Type t;
}	
