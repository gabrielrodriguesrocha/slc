package jslc.AST;

public class FloatT extends PostfixExpr {
	public FloatT (float val) {
		this.val = val;
	}

	public void genC (PW pw) {
		pw.out.print(String.valueOf(val));
	}

	public Type getType () {
		return Type.floatType;
	}

	private float val;
}
