package jslc.AST;

public class StringDecl extends Decl {
    public StringDecl (Type type, String id, String val) {
		this.type = type;
        this.id = id;
        this.val = val;
    }

    public void genC (PW pw) {
        pw.println (type.getCname() + " " + id + " = \"" + val + "\";");
    }

	private Type type;
    private String id;
    private String val;
}
