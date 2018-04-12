package jslc.AST;

import java.util.*;
import jslc.Lexer.Symbol;

public class Param {
	public Param (Symbol type, Symbol name) {
		this.type = type;
		this.name = name;
	}
	public genC(PW pw){
		
	}

	private Symbol type;
	private Symbol name;

}
