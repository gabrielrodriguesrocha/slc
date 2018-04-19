package jslc.AST;

public class Identifier extends PostfixExpr {
	public Identifier (String val) {
		this.val = val;
	}

	public void genC (PW pw) {
		pw.out.print(val);
	}

	private String val;
}
