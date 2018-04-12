package jslc.AST;

public class Integer extends PostfixExpr {
	public Integer (int val) {
		this.val = val;
	}

	public void genC (PW pw) {
			pw.println(val);
	}

	private int val;
}
