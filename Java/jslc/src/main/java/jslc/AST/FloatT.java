package jslc.AST;

public class FloatT extends PostfixExpr {
	public FloatT (float val) {
		this.val = val;
	}

	public void genC (PW pw) {
		pw.print(String.valueOf(val));
	}

	private float val;
}