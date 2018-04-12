package jslc.AST;

public class FloatingPoint extends PostfixExpr {
	public FloatingPoint (int val) {
		this.val = val;
	}

	public void genC () {
			
	}

	private int val;
}
