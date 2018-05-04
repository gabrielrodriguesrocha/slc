package jslc.AST;

import java.util.*;

public class Function implements Typeable{
	public Function (Type type, ArrayList<Param> params) {
		this.type = type;
		this.params = params;
	}

	public Type getType() {
		return type;
	}

	private Type type;
	private ArrayList<Param> params;
}
