package jslc.AST;

import java.util.*;

public class VarDecl extends Decl {
    public VarDecl (Type type, Hashtable<String, NamedTypeable> idList) {
        this.type = type;
        this.idList = idList;
    }

    public void genC (PW pw) {
		pw.print(type.getCname() + " ");
        Collection<NamedTypeable> col = idList.values();
		Iterator<NamedTypeable> itr = col.iterator();
		pw.out.print(itr.next().getIdentifier());
		while (itr.hasNext()) {
			pw.out.print(", ");
			pw.out.print(itr.next().getIdentifier());
        }
        pw.out.println(";");
    }

    private Type type;
    private Hashtable<String, NamedTypeable> idList;
}
