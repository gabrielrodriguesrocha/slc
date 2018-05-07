package jslc.AST;

import java.util.*;

public class Function implements NamedTypeable{
	public Function (String identifier, Type type, ArrayList<Param> params) {
		this.identifier = identifier;
		this.type = type;
		this.params = params;
	}

	public Type getType() {
		return type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public ArrayList<Param> getParams() {
		return params;
	}

	private Type type;
	private ArrayList<Param> params;
	private String identifier;
}
