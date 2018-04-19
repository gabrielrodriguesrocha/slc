package jslc.AST;

import java.util.*;

public class WriteStmt extends Stmt {
	public WriteStmt (ArrayList<String> idList) {
		this.idList = idList;
	}

	public void genC(PW pw) {
		pw.print("printf(");
		Iterator<String> itr = idList.iterator();
		pw.out.print(itr.next());
		while (itr.hasNext()) {
			pw.out.print(", ");
			pw.out.print(itr.next());
		}
		pw.out.println(");");
	}

	private ArrayList<String> idList;
}
