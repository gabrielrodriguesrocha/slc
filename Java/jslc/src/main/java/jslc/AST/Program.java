package jslc.AST;

import java.util.*;

public class Program {
	public Program(ArrayList<Decl> d, ArrayList<FuncDecl> f) {
		this.d = d;
		this.f = f;	
	}

	public void genC(PW pw) {
		pw.out.println("#include <stdio.h>");
		for (Decl a : d) {
			a.genC(pw);
		}
		for (FuncDecl b : f) {
			b.genC(pw);
		}
	}

	private ArrayList<Decl> d;
	private ArrayList<FuncDecl> f;
}
