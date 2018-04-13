package jslc.AST;

public class StringDecl extends Decl {
    public StringDecl (String id, String val) {
        this.id = id;
        this.val = val;
    }

    public void genC (PW pw) {
        pw.println ("char [" + val.length() + "] " + id + " = \"" + val + "\";");
    }

    private String id;
    private String val;
}