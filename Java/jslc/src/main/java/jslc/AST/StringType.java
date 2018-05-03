package jslc.AST;

import jslc.Lexer.Symbol;

public class StringType extends Type {
	public StringType () {
		super(Symbol.STRING);
	}
	public String getCname() {
		return "char *";
	}

	private Symbol name;
}
