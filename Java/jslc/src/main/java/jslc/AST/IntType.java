package jslc.AST;

import jslc.Lexer.Symbol;

public class IntType extends Type {
	public IntType () {
		super(Symbol.INT);
	}
	public String getCname() {
		return "int";
	}
}
