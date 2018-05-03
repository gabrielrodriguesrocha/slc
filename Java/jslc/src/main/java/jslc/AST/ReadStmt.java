package jslc.AST;

import java.util.*;

public class ReadStmt extends Stmt {
	public ReadStmt	(Hashtable<String, Object> idList) {
		this.idList = idList;
	}

	public void genC(PW pw) {
		pw.print("scanf(\"\",");
		Collection<Object> col = idList.values();
		Iterator<Object> itr = col.iterator();
		pw.out.print(itr.next());
		while (itr.hasNext()) {
			pw.out.print(", ");
			pw.out.print(itr.next());
		}
		pw.out.println(");");
	}

	private Hashtable<String, Object> idList;
}
