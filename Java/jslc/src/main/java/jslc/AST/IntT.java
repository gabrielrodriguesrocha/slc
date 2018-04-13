package jslc.AST;

public class IntT extends PostfixExpr {
	public IntT (int val) {
		this.val = val;
	}

	public void genC (PW pw) {
		pw.print(String.valueOf(val));
	}

	private int val;
}
