package jslc.AST;

public class Program {
	public Program(Decl d, FuncDecl f) {
		this.d = d;
		this.f = f;	
	}

	public void genC() {
		d.genC();
		f.genC();
	}

	private Decl d;
	private FuncDecl f;
}