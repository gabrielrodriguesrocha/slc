package jslc.AST;

public class Identifier extends PostfixExpr {
	public Identifier (String val) {
		this.val = val;
	}

	public void genC () {
			
	}

	private String val;
}
