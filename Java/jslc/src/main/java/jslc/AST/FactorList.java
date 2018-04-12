package jslc.AST;

import jslc.Lexer.Symbol;

public class FactorList {
	public FactorList (Symbol op, Factor e) {
		this.op = op;
		this.e = e;
	}

	public void genC () {

	}

	private Symbol op;
	private Factor e;
}
