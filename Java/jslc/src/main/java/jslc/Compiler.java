/*
{x} : x is optional
CAPS : CAPS is a token (terminal) made up of one or more characters.  
small case symbols are non-terminals.

Program 
program           -> PROGRAM id BEGIN pgm_body END 
id                -> IDENTIFIER
pgm_body          -> decl func_declarations
decl		  -> string_decl_list {decl} | var_decl_list {decl} | empty

Global String Declaration 
string_decl_list  -> string_decl {string_decl_tail}
string_decl       -> STRING id := str ; | empty
str               -> STRINGLITERAL
string_decl_tail  -> string_decl {string_decl_tail}

Variable Declaration 
var_decl_list     -> var_decl {var_decl_tail} 
var_decl          -> var_type id_list ; | empty
var_type	  -> FLOAT | INT
any_type          -> var_type | VOID 
id_list           -> id id_tail
id_tail           -> , id id_tail | empty
var_decl_tail     -> var_decl {var_decl_tail}

Function Paramater List 
param_decl_list   -> param_decl param_decl_tail
param_decl        -> var_type id
param_decl_tail   -> , param_decl param_decl_tail | empty

Function Declarations 
func_declarations -> func_decl {func_decl_tail}
func_decl         -> FUNCTION any_type id ({param_decl_list}) BEGIN func_body END | empty
func_decl_tail    -> func_decl {func_decl_tail} 
func_body         -> decl stmt_list 

Statement List 
stmt_list         -> stmt stmt_tail | empty
stmt_tail         -> stmt stmt_tail | empty
stmt              -> assign_stmt | read_stmt | write_stmt | return_stmt | if_stmt | for_stmt | call_stmt

Basic Statements 
assign_stmt       -> assign_expr ;
assign_expr       -> id := expr
read_stmt         -> READ ( id_list );
write_stmt        -> WRITE ( id_list );
return_stmt       -> RETURN expr ;
call_stmt		  -> id ( {expr_list} );

Expressions 
expr              -> factor expr_tail 
expr_tail         -> addop factor expr_tail | empty
factor            -> postfix_expr factor_tail
factor_tail       -> mulop postfix_expr factor_tail | empty
postfix_expr      -> primary | call_expr
call_expr         -> id ( {expr_list} )
expr_list         -> expr expr_list_tail
expr_list_tail    -> , expr expr_list_tail | empty
primary           -> (expr) | id | INTLITERAL | FLOATLITERAL
addop             -> + | -
mulop             -> * | 

Complex Statements and Condition 
if_stmt           -> IF ( cond ) THEN stmt_list else_part ENDIF
else_part         -> ELSE stmt_list | empty
cond              -> expr compop expr
compop            -> < | > | =
for_stmt          -> FOR ({assign_expr}; {cond}; {assign_expr}) stmt_list ENDFOR


an IDENTIFIER token will begin with a letter, and be followed by up to 30 letters and numbers.  
IDENTIFIERS are case sensitive. 

INTLITERAL: integer number 
            ex) 0, 123, 678
FLOATLITERAL: floating point number available in two different format
                yyyy.xxxxxx or .xxxxxxx
            ex) 3.141592 , .1414 , .0001 , 456.98

STRINGLITERAL  (Max 80 characters including '\0')
        :      anything sequence of character except '"' 
            between '"' and '"' 
            ex) "Hello world!" , "***********" , "this is a string"

COMMENT:
      Starts with "--" and lasts till the end of line
      ex) -- this is a comment
      ex) -- any thing after the "--" is ignored 
*/


package jslc;

import jslc.Lexer.*;

import jslc.Error.*;

public class Compiler {

	// para geracao de codigo
	public static final boolean GC = false; 

    public void compile( char []p_input ) {
        lexer = new Lexer(p_input, error);
		error = new CompilerError(null);
		error.setLexer(lexer);
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
        funcDecl();
        if (lexer.token != Symbol.END)
            error.signal("Esperava end");
    }

    // Decl ::= StringDeclList {Decl} | StringDeclList {Decl} | empty
    public void decl() {
        while (lexer.token == Symbol.STRING || varType()) {
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
                error.signal("Esperava ';''");
			lexer.nextToken();
        }
    }

    public void idList() {
        while (lexer.token == Symbol.IDENT) {
            lexer.nextToken();
            if (lexer.token != Symbol.COMMA)
                return;
            lexer.nextToken();
        }
        error.signal("Não é um identificador válido");
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
			if (lexer.token != Symbol.STRINGLITERAL)
                error.signal("Esperava string literal");
            lexer.nextToken();
            if (lexer.token != Symbol.SEMICOLON)
                error.signal("Esperava ;");
            lexer.nextToken();
        }
    }

    public boolean anyType() {
        return varType() || lexer.token == Symbol.VOID;
    }
    
    public void funcDecl() {
        while(lexer.token == Symbol.FUNCTION) {
            lexer.nextToken();
            if (!anyType())
                error.signal("Esperava tipo da função");
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT)
                error.signal("Esperava identificador");
            lexer.nextToken();
		    if (lexer.token != Symbol.LPAR)
				error.signal("Esperava '('");
			lexer.nextToken();
			paramDeclList();
			if (lexer.token != Symbol.RPAR)
				error.signal("Esperava ')'");
			lexer.nextToken();
			if (lexer.token != Symbol.BEGIN)
				error.signal("Esperava begin");
			lexer.nextToken();
			decl();
			stmtList();
			if (lexer.token != Symbol.END)
				error.signal("Esperava end");
			lexer.nextToken();	
        }
    }

	public void paramDeclList() {
		while (varType()) {
			lexer.nextToken();
			if (lexer.token != Symbol.IDENT)
				error.signal("Esperava identificador");
			lexer.nextToken();
			if (lexer.token != Symbol.COMMA)
				break;
			lexer.nextToken();
		}
	}

	public boolean statementSymbol() {
		return lexer.token == Symbol.IDENT ||
			   lexer.token == Symbol.READ ||
			   lexer.token == Symbol.WRITE ||
			   lexer.token == Symbol.RETURN ||
			   lexer.token == Symbol.IF ||
			   lexer.token == Symbol.FOR;
	}

	public void stmtList() {
		while (statementSymbol()) {
			if (lexer.token == Symbol.IDENT) {
				lexer.lookAhead(); // danger lies in breaking patterns
				if (lexer.token == Symbol.ASSIGN) {
					lexer.rollback();
					assignStmt();
				}
				else if (lexer.token == Symbol.LPAR) {
					lexer.rollback();
					callStmt();
				}
				else
					error.signal("Esperava atribuição ou chamada de função");
			}
			else if (lexer.token == Symbol.READ) {
				readStmt();
			}
			else if (lexer.token == Symbol.WRITE) {
				writeStmt();
			}
			else if	(lexer.token == Symbol.RETURN) {
				returnStmt();
			}
			else if (lexer.token == Symbol.IF) {
				ifStmt();
			}
			else if (lexer.token == Symbol.FOR) {
				forStmt();
			}
		}
	}

	public void assignStmt() {
		assignExpr();
		//lexer.nextToken();
		if(lexer.token != Symbol.SEMICOLON)
			error.signal ("Esperava ';'");
		lexer.nextToken();
	}

	public void callStmt() {
		callExpr();
		if(lexer.token != Symbol.SEMICOLON)
			error.signal ("Esperava ';'");
		lexer.nextToken();
	}

	public void callExpr() {
		if (lexer.token != Symbol.IDENT)
			error.signal("Esperava identificador");
		lexer.nextToken();
		if (lexer.token != Symbol.LPAR)
			error.signal ("Esperava '('");
		lexer.nextToken();
		exprList();
		if(lexer.token != Symbol.RPAR)
			error.signal ("Esperava ')'");
		lexer.nextToken();

	}
	
	public void readStmt() {
		if (lexer.token != Symbol.READ)
			error.signal("Esperava read");
		lexer.nextToken();
		if (lexer.token != Symbol.LPAR)
			error.signal ("Esperava '('");
		lexer.nextToken();
		idList();
		if(lexer.token != Symbol.RPAR)
			error.signal ("Esperava ')'");
		lexer.nextToken();
		if(lexer.token != Symbol.SEMICOLON)
			error.signal ("Esperava ';'");
		lexer.nextToken();
	}
	
	public void writeStmt() {
		if (lexer.token != Symbol.WRITE)
			error.signal("Esperava read");
		lexer.nextToken();
		if (lexer.token != Symbol.LPAR)
			error.signal ("Esperava '('");
		lexer.nextToken();
		idList();
		if(lexer.token != Symbol.RPAR)
			error.signal ("Esperava ')'");
		lexer.nextToken();
		if(lexer.token != Symbol.SEMICOLON)
			error.signal ("Esperava ';'");
		lexer.nextToken();
	}

	public void returnStmt() {
		if (lexer.token != Symbol.RETURN)
			error.signal("Esperava return");
		lexer.nextToken();
		expr();
		if (lexer.token != Symbol.SEMICOLON)
			error.signal("Esperava ';'");
		lexer.nextToken();
	}

	public void ifStmt() {
		if (lexer.token != Symbol.IF)
			error.signal("Esperava if");
		lexer.nextToken();
		if (lexer.token != Symbol.LPAR)
			error.signal("Esperava '('");
		lexer.nextToken();
		compExpr();
		if (lexer.token != Symbol.RPAR)
			error.signal("Esperava ')'");
		lexer.nextToken();
		if (lexer.token != Symbol.THEN)
			error.signal("Esperava then");
		lexer.nextToken();
		stmtList();
		if (lexer.token == Symbol.ELSE) {
			elseStmt();
		}
		else if (lexer.token != Symbol.ENDIF) {
			error.signal("Esperava endif");	
		}
		lexer.nextToken();
	}

	public void elseStmt() {
		if (lexer.token != Symbol.ELSE)
			error.signal("Esperava else");
		lexer.nextToken();
		stmtList();
	}

	public void compExpr () {
		expr();
		//lexer.nextToken();
		if (!compop())
			error.signal("Esperava >, < ou =");
		lexer.nextToken();
		expr();
	}

	public boolean compop () {
		return lexer.token == Symbol.GT ||
			   lexer.token == Symbol.LT ||
		       lexer.token == Symbol.EQUAL;	   
	}

	public void forStmt() {
		if (lexer.token != Symbol.FOR)
			error.signal("Esperava for");
		lexer.nextToken();
		if (lexer.token != Symbol.LPAR)
			error.signal("Esperava '(");
		lexer.nextToken();
		assignExpr();
		if (lexer.token != Symbol.SEMICOLON)
			error.signal("Esperava ';'");
		lexer.nextToken();
		compExpr();
		if (lexer.token != Symbol.SEMICOLON)
			error.signal("Esperava ';'");
		lexer.nextToken();
		assignExpr();
		if (lexer.token != Symbol.RPAR)
			error.signal("Esperava ')'");
		lexer.nextToken();
		stmtList();
		if (lexer.token != Symbol.ENDFOR)
			error.signal("Esperava endfor");
		lexer.nextToken();
	}

	public void assignExpr() {
		if (lexer.token != Symbol.IDENT)
			error.signal("Esperava identificador");
		lexer.nextToken();
		if (lexer.token != Symbol.ASSIGN)
			error.signal("Esperava :=");
		lexer.nextToken();
		expr();	
	}
    
	public void exprList() {
		while(lexer.token == Symbol.IDENT ||
			  lexer.token == Symbol.LPAR ||
			  lexer.token == Symbol.FLOATLITERAL ||
			  lexer.token == Symbol.INTLITERAL) {
			expr();
			if (lexer.token != Symbol.COMMA)
				break;
			lexer.nextToken();
		}
	}
	
	public boolean addop() {
		return lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS;
	}

	public boolean mulop() {
		return lexer.token == Symbol.MULT || lexer.token == Symbol.DIV;
	}
	
	public void expr() {
		factor();
		while (addop()) {
			lexer.nextToken();
			factor();
		}
	}

	public void factor() {
		postfixExpr();
		while (mulop()) {
			lexer.nextToken();
			postfixExpr();
		}
	}

	public void postfixExpr() {
		if (lexer.token == Symbol.INTLITERAL ||
			lexer.token == Symbol.FLOATLITERAL) {
			lexer.nextToken();
		}
		else if (lexer.token == Symbol.IDENT) {
			lexer.lookAhead(); // danger lies in breaking patterns
			if (lexer.token == Symbol.LPAR) {
				lexer.rollback();
				callExpr();
			}
		}
		else if (lexer.token == Symbol.LPAR) {
			lexer.nextToken();
			expr();
			//lexer.nextToken();
			if (lexer.token != Symbol.RPAR)
				error.signal("Esperava ')'");
		}
		else
			error.signal("Problema na sintaxe");
	}
    
	private Lexer lexer;
    private CompilerError error;

}
    
    
    
