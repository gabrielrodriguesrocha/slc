package jslc.AST;

public class Variable {
	public Variable (Type type, String identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	private Type type;
	private String identifier;
}
