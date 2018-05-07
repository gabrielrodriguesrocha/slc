package jslc.AST;

public class Variable extends PostfixExpr implements NamedTypeable{
	public Variable (Type type, String identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	public Type getType() {
		return type;
	}
	public String getIdentifier(){
		return identifier;
	}

	public void genC (PW pw) {
		pw.out.print(identifier);
	}

	private Type type;
	private String identifier;
}
