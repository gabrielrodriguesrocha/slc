package jslc.AST;

import jslc.Lexer.Symbol;

public class VoidType extends Type {
	public VoidType () {
		super(Symbol.VOID);
	}
	public String getCname() {
		return "void";
	}
}
