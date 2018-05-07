package jslc.AST;

import java.util.*;

public class ReadStmt extends Stmt {
	public ReadStmt	(ArrayList<Variable> idList) {
		this.idList = idList;
	}

	public void genC(PW pw) {
		pw.print("scanf(\"");
		Iterator<Variable> itr = idList.iterator(); //iterador para as variaveis
		
		if (itr.hasNext()) {
			walk = itr.next();
			pw.out.print(typePrint(walk));
			while(itr.hasNext()) {
				pw.out.print(" ");
				walk = itr.next();
				pw.out.print(typePrint(walk));
			}

		}

		pw.out.print("\", ");
		
		itr = idList.iterator();
		walk = itr.next();
		if(walk.getType() instanceof FloatType ||
		   walk.getType() instanceof IntType){
			pw.out.print("&");
		}
		pw.out.print(walk.getIdentifier());
		while (itr.hasNext()) {
			pw.out.print(", ");
			walk = itr.next();
			if(walk.getType() instanceof FloatType ||
			   walk.getType() instanceof IntType){
				pw.out.print("&");
			}
			pw.out.print(walk.getIdentifier());
		}
		pw.out.println(");");
	}
	private String typePrint(Variable a){ //de acordo com o tipo de variavel volta a flag para o printf do C
		if(a.getType() instanceof FloatType){
			return "%f";
		}else if(a.getType() instanceof IntType){
			return "%d";
		}else{
			return "%s";
		}
		
	}
	private ArrayList<Variable> idList;
	private Variable walk;
}
