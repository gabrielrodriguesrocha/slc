package jslc.AST;

import java.util.*;

public class VarDecl extends Decl {
    public VarDecl (Type type, Hashtable<String, Object> idList) {
        this.type = type;
        this.idList = idList;
    }

    public void genC (PW pw) {
		pw.print(type.getCname() + " ");
        Collection<Object> col = idList.values();
		Iterator<Object> itr = col.iterator();
		pw.out.print(itr.next());
		while (itr.hasNext()) {
			pw.out.print(", ");
			pw.out.print(itr.next());
        }
        pw.out.println(";");
    }

    private Type type;
    private Hashtable<String, Object> idList;
}
