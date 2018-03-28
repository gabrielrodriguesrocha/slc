/*

*/


package jslc;

import jslc.Lexer.*;

import javax.swing.text.DefaultStyledDocument.ElementSpec;

import jslc.Error.*;

public class Compiler {

	// para geracao de codigo
	public static final boolean GC = false; 

    public void compile( char []p_input ) {
        lexer = new Lexer(p_input, error);
        lexer.nextToken();
        program();
    }
    
    // Program ::= Decl FuncDecl
    public void program(){
        if (lexer.token != Symbol.PROGRAM)
            error.signal("Esperava program");
        lexer.nextToken();
        if (lexer.token != Symbol.IDENT)
            error.signal("Esperava nome do programa");
        lexer.nextToken();
        if (lexer.token != Symbol.BEGIN)
            error.signal("Esperava begin");
        lexer.nextToken();
        decl();
        lexer.nextToken();
        funcDecl();
        lexer.nextToken();
        if (lexer.token != Symbol.END)
            error.signal("Esperava end");
    }

    // Decl ::= StringDeclList {Decl} | StringDeclList {Decl} | empty
    public void decl() {
        while (lexer.token == Symbol.STRING ||
               lexer.token == Symbol.FLOAT ||
               lexer.token == Symbol.INT) {
            if (lexer.token == Symbol.STRING) {
                stringDeclList();
            }
            else if (varType()) { //perhaps refactor
                varDeclList();
            }
        }
        // no error is thrown since decl can be empty
    }

    public boolean varType() {
        return lexer.token == Symbol.FLOAT || lexer.token == Symbol.INT;
    }
       
	// VarDecList ::= VarType IdList ; | empty
	public void varDeclList(){
        while (varType()) {
            lexer.nextToken();
            idList();
            if (lexer.token != Symbol.SEMICOLON)
                error.signal("Esperava ';''")
        }
    }

    public void idList() {
        while (lexer.token == Symbol.IDENT) {
            lexer.nextToken();
            if (lexer.token != Symbol.COMMA)
                return;
            lexer.nextToken();
        }
        error.signal("Declaração de variável com palavra reservada");
    }
    
    public void stringDeclList(){
        while(lexer.token == Symbol.STRING) {
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT)
                error.signal("Declaração de variável com palavra reservada");
            lexer.nextToken();
            if (lexer.token != Symbol.ASSIGN)
                error.signal("Esperava ':=''");
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT)
                error.signal("Esperava string ligeral");
            lexer.nextToken();
            if (lexer.token != Symbol.SEMICOLON)
                error.signal("Esperava ;");
            lexer.nextToken();
        }
    }

    public boolean anyType() {
        return anyType() || lexer.token == Symbol.VOID;
    }
    
    public void funcDecl() {
        while(lexer.token == Symbol.FUNCTION) {
            lexer.nextToken();
            if (!anyType())
                error.signal("Esperava tipo da função");
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT)
                error.signal("Esperava identificador");
            //
        }
    }

    //AssignStatement ::= Variable '=' Expr ';'
    public void assignStatement(){

        
    }
    
    // Expr ::= Oper  Expr Expr  | Number
    public void expr(){

        
    }
    
	private Lexer lexer;
    private CompilerError error;

}
    
    
    