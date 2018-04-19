package jslc.AST;

import jslc.Lexer.Symbol;

public abstract class Type {
	public Type (Symbol name) {
		this.name = name;
	}

	public static Type stringType = new StringType ();
	public static Type intType = new IntType ();
	public static Type floatType = new FloatType ();
	public static Type voidType = new VoidType ();

	public String getname() {
		return name.toString();
	}

	abstract public String getCname();

	private Symbol name;
}
