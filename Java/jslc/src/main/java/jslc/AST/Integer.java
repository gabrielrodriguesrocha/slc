package jslc.AST;

public class Integer extends PostfixExpr {
	public Integer (int val) {
		this.val = val;
	}

	public void genC () {
			
	}

	private int val;
}
