package jslc.AST;

import java.util.*;

import jslc.Lexer.Symbol;

public class VarDecl extends Decl {
    public VarDecl (Symbol type, ArrayList<String> idList) {
        this.type = type;
        this.idList = idList;
    }

    public void genC (PW pw) {
        pw.print(type.toString() + " ");
        Iterator<String> itr = idList.iterator();
		pw.print(itr.next());
		while (itr.hasNext()) {
			pw.print(", ");
			pw.print(itr.next());
        }
        pw.println(";");
    }

    private Symbol type;
    private ArrayList<String> idList;
}