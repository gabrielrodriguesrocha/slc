package jslc.AST;

import java.util.*;

import jslc.Lexer.Symbol;

public class VarDecl extends Decl {
    public VarDecl (Type type, ArrayList<String> idList) {
        this.type = type;
        this.idList = idList;
    }

    public void genC (PW pw) {
		pw.print(type.getCname() + " ");
        Iterator<String> itr = idList.iterator();
		pw.print(itr.next());
		while (itr.hasNext()) {
			pw.print(", ");
			pw.print(itr.next());
        }
        pw.println(";");
    }

    private Type type;
    private ArrayList<String> idList;
}
