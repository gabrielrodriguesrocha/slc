package jslc.AST;

public abstract class Decl {
	public Decl () {}
	public abstract void genC(PW pw);
}
