package jslc.AST;

import java.util.*;

public class Function {
	public Function (Type type, ArrayList<Param> params) {
		this.type = type;
		this.params = params;
	}

	private Type type;
	private ArrayList<Param> params;
}
