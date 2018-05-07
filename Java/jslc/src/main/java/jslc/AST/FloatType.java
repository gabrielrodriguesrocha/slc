package jslc.AST;

import jslc.Lexer.Symbol;

public class FloatType extends Type {
	public FloatType () {
		super(Symbol.FLOAT);
	}
	public String getCname() {
		return "float";
	}
}
