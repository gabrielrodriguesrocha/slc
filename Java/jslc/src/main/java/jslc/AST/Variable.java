package jslc.AST;

public class Variable implements NamedTypeable{
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

	private Type type;
	private String identifier;
}
