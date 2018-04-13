package jslc.AST;

import java.util.*;

public class WriteStmt extends Stmt {
	public WriteStmt (ArrayList<String> idList) {
		this.idList = idList;
	}

	public void genC(PW pw) {
		pw.print("printf(");
		Iterator<String> itr = idList.iterator();
		pw.print(itr.next());
		while (itr.hasNext()) {
			pw.print(", ");
			pw.print(itr.next());
		}
		pw.println(");");
	}

	private ArrayList<String> idList;
}
