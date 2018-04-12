package jslc.AST;

import jslc.Lexer.Symbol;

import java.util.*;

public class WriteStmt extends Stmt {
	public WriteStmt (ArrayList<Symbol> idList) {
		this.idList = idList;
	}

	public void genC() {

	}

	private ArrayList<Symbol> idList;
}
