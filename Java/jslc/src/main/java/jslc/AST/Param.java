package jslc.AST;

import jslc.Lexer.Symbol;

public class Param {
	public Param (Symbol type, String name) {
		this.type = type;
		this.name = name;
	}
	public void genC(PW pw){
		pw.print(type.toString() + " " + name);
	}

	private Symbol type;
	private String name;

}
