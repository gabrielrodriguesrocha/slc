package jslc.AST;

import java.util.*;

public class ReadStmt extends Stmt {
	public ReadStmt	(ArrayList<String> idList) {
		this.idList = idList;
	}

	public void genC(PW pw) {
		pw.print("scanf(");
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
