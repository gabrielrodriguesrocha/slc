package jslc.AST;

public class Param {
	public Param (Type type, String name) {
		this.type = type;
		this.name = name;
	}
	public void genC(PW pw){
		pw.out.print(type.getCname() + " " + name);
	}

	private Type type;
	private String name;

}
