package jslc.AST;

import jslc.Lexer.Symbol;

import java.util.*;

public class FuncDecl {
	public FuncDecl (Symbol type, ArrayList<Param> params, FuncBody body) {
		this.type = type;
		this.params = params;
		this.body = body;
	}

	public void genC() {
	}

	private Symbol type;
	private ArrayList<Param> params;
	private FuncBody body;
}
