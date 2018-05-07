package jslc.AST;

public class Param implements Typeable {
	public Param (Type type, String name) {
		this.type = type;
		this.name = name;
	}
	public void genC(PW pw){
		pw.out.print(type.getCname() + " " + name);
	}

	public Type getType() {
		return type;
	}

	public String getIdentifier() {
		return name;
	}

	private Type type;
	private String name;

}
