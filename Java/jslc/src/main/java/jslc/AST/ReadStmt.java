package jslc.AST;

import java.util.*;

public class ReadStmt extends Stmt {
	public ReadStmt	(ArrayList<Variable> idList) {
		this.idList = idList;
	}

	public void genC(PW pw) {
		boolean[] isString = new boolean[idList.size()];
		int i = 0;
		pw.print("scanf(\"");
		Iterator<Variable> itr = idList.iterator(); //iterador para as variaveis
		
		for(Variable a: idList){
			if((((a.getType())).getname()).equals("string")){
				isString[i] = true;
				
			}else{
				isString[i] = false;
				
			}
			i++;
			pw.out.print(" ");
			pw.out.print(typePrint(a));
		}
		
		if(isString[0] == false){
			pw.out.print("&");
		}
		pw.out.print((itr.next()).getIdentifier());
		i = 1;
		while (itr.hasNext()) {
			pw.out.print(", ");
			if(isString[i] == false){
				pw.out.print("&");
			}
			pw.out.print((itr.next()).getIdentifier());
			i++;
		}
		pw.out.println(");");
	}
	private String typePrint(Variable a){ //de acordo com o tipo de variavel volta a flag para o printf do C
		if(((a.getType()).getname()).equals("float")){
			return "%f";
		}else if(((a.getType()).getname()).equals("int")){
			return "%d";
		}else{
			return "%s";
		}
		
	}
	private ArrayList<Variable> idList;
}
