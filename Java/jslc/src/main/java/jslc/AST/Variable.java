package jslc.AST;

public class Variable implements Typeable{
	public Variable (Type type, String identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	public Type getType() {
		return type;
	}

	private Type type;
	private String identifier;
}
