package jslc.AST;

import jslc.Lexer.Symbol;

import java.util.*;

public class ReadStmt extends Stmt {
	public ReadStmt	(ArrayList<Symbol> idList) {
		this.idList = idList;
	}

	public void genC() {

	}

	private ArrayList<Symbol> idList;
}
