package jslc.AST;

import java.util.*;

public class WriteStmt extends Stmt {
	public WriteStmt (ArrayList<Variable> idList) {
		this.idList = idList;
	}

	public void genC(PW pw) {
		boolean first = true;
		pw.print("printf(\"");
		Iterator<Variable> itrV = idList.iterator(); //iterador para variaveis
		
		//printa o primeiro tipo
		for(Variable a: idList){
			if(first != true){
				pw.out.print(" ");
			}
			pw.out.print(typePrint(a));
			first = false;
		}
		pw.out.print("\", "); //fecha " e coloca a ,
		pw.out.print((itrV.next()).getIdentifier()); //printa o primeiro nome de variavel
		
		while (itrV.hasNext()) { //verifica se existem mais variaveis e printa 
			pw.out.print(", ");
			pw.out.print((itrV.next()).getIdentifier());
		}
		pw.out.println(");"); //fecha tudo
	}
	
	private String typePrint(Variable a){ //de acordo com o tipo de variavel volta a flag para o printf do C
		if(a.getType() instanceof FloatType) {
			return "%f";
		}
		else if(a.getType() instanceof IntType) {
			return "%d";
		}
		else {
			return "%s";
		}	
	}

	private ArrayList<Variable> idList;
}
