package jslc;

import jslc.Lexer.*;

import jslc.Error.*;

/*
	Convenção de notação:
	
	A ::= B | {C} | [D]+ | [E]* 
	
	significa:
	1) A deriva B OU
	2) A deriva opcionalmente C OU
	3) A deriva uma ou mais repetições de D OU
	4) A deriva zero ou mais repetições de E.
*/

public class Compiler {

	// para geracao de codigo
	public static final boolean GC = false; 

    public void compile( char []p_input ) {
		error = new CompilerError(null);
        lexer = new Lexer(p_input, error);
		error.setLexer(lexer);
        lexer.nextToken();
        program();
    }
    
    // Program ::= Decl FuncDecl
    public void program(){
        if (lexer.token != Symbol.PROGRAM)
            error.signal("Esperava PROGRAM");
        lexer.nextToken();
        if (lexer.token != Symbol.IDENT)
            error.signal("Esperava nome do programa");
        lexer.nextToken();
        if (lexer.token != Symbol.BEGIN)
            error.signal("Esperava BEGIN");
		lexer.nextToken();
		if (varType() || lexer.token == Symbol.STRING)
			decl();
		if (lexer.token == Symbol.FUNCTION)
        	funcDecl();
        if (lexer.token != Symbol.END)
            error.signal("Esperava declaração de variável, função ou END");
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
		 if (!varType() &&
			  lexer.token != Symbol.STRING &&
			  lexer.token != Symbol.FUNCTION &&
			  !statementSymbol())
		 error.signal("Tipo de variável não suportado, esperava STRING, INT ou FLOAT");
    }

	// VarType ::= FLOAT | INT
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
	
	// IdList ::= Id [, IdList]* | Id
    public void idList() {
        while (lexer.token == Symbol.IDENT) {
            lexer.nextToken();
            if (lexer.token != Symbol.COMMA)
                return;
            lexer.nextToken();
        }
        error.signal("Não é um identificador válido");
    }
    
	// StringDeclList ::= [STRING id := STRINGLITERAL ;]*
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

	// AnyType ::= VarType | VOID
    public boolean anyType() {
		if (varType() || lexer.token == Symbol.VOID)
			return true;
		else
			error.signal("Tipo de função não suportado, esperava INT, FLOAT ou VOID");
        return false;
    }
    
	// FuncDecl ::= [FUNCTION AnyType ([ParamDeclList]*) BEGIN FuncBody END]*
    public void funcDecl() {
        while(lexer.token == Symbol.FUNCTION) {
            lexer.nextToken();
            if (!anyType())
                error.signal("Tipo de função não suportado, esperava INT, FLOAT ou VOID");
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
			if (varType() || lexer.token == Symbol.STRING)
				decl();
			if (statementSymbol())
				stmtList();
			if (lexer.token != Symbol.END)
				error.signal("Esperava end");
			lexer.nextToken();	
        }
    }

	// ParamDeclList ::= [VarType Id ,]* | VarType Id
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

	// StmtList ::= [AssignStmt | CallStmt | ReadStmt | WriteStmt | ReturnStmt | IfStmt | ForStmt]*
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

	// AssignStmt ::= AssignExpr ;
	public void assignStmt() {
		assignExpr();
		//lexer.nextToken();
		if(lexer.token != Symbol.SEMICOLON)
			error.signal ("Esperava ';'");
		lexer.nextToken();
	}

	// CallStmt ::= CallExpr ;
	public void callStmt() {
		callExpr();
		if(lexer.token != Symbol.SEMICOLON)
			error.signal ("Esperava ';'");
		lexer.nextToken();
	}
	
	// CallExpr ::= Id ( ExprList )
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
	
	// ReadStmt ::= READ ( IdList ) ;
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
	
	// ReadStmt ::= WRITE ( IdList ) ;
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

	// ReturnStmt ::= RETURN Expr;
	public void returnStmt() {
		if (lexer.token != Symbol.RETURN)
			error.signal("Esperava return");
		lexer.nextToken();
		expr();
		if (lexer.token != Symbol.SEMICOLON)
			error.signal("Esperava ';'");
		lexer.nextToken();
	}

	// IfStmt ::= IF ( CompExpr ) THEN StmtList {ElseStmt} ENDIF
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

	// ElseStmt ::= ELSE StmtList
	public void elseStmt() {
		if (lexer.token != Symbol.ELSE)
			error.signal("Esperava else");
		lexer.nextToken();
		stmtList();
	}

	// CompExpr ::= Expr CompOp Expr
	public void compExpr () {
		expr();
		//lexer.nextToken();
		if (!compop())
			error.signal("Esperava >, < ou =");
		lexer.nextToken();
		expr();
	}

	// CompOp ::= > | < | =
	public boolean compop () {
		return lexer.token == Symbol.GT || lexer.token == Symbol.LT || lexer.token == Symbol.EQUAL;
	}

	// ForStmt ::= FOR ({AssignExpr} ; {CompExpr} ; {AssignExpr}) StmtList ENDFOR
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

	// AssignExpr ::= Id := Expr
	public void assignExpr() {
		if (lexer.token != Symbol.IDENT)
			error.signal("Esperava identificador");
		lexer.nextToken();
		if (lexer.token != Symbol.ASSIGN)
			error.signal("Esperava :=");
		lexer.nextToken();
		expr();	
	}
	
    // ExprList ::= [Expr]+ 
	public void exprList() {
		if (lexer.token != Symbol.IDENT &&
			lexer.token != Symbol.LPAR &&
			lexer.token != Symbol.FLOATLITERAL &&
			lexer.token != Symbol.INTLITERAL)
			error.signal("Esperava expressão");
	
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
	
	// AddOp ::= + | -
	public boolean addop() {
		return lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS;
	}

	// MulOp ::= * | /
	public boolean mulop() {
		return lexer.token == Symbol.MULT || lexer.token == Symbol.DIV;
	}
	
	// Expr ::= Factor [AddOp Factor]*
	public void expr() {
		factor();
		while (addop()) {
			lexer.nextToken();
			factor();
		}
		/*if (!addop() && !compop() && lexer.token != Symbol.RPAR)
			error.signal("Operador não suportado, esperava + ou -");*/
	}

	// Factor ::= PostfixExpr [MulOp PostfixExpr]*
	public void factor() {
		postfixExpr();
		while (mulop()) {
			lexer.nextToken();
			postfixExpr();
		}
		/*if (!mulop() && !compop() && lexer.token != Symbol.RPAR)
			error.signal("Operador não suportado, esperava * ou /");*/
	}

	// PostfixExpr ::= INTLITERAL | FLOATLITERAL | Id | ( Expr ) | CallExpr
	public void postfixExpr() {
		if (lexer.token == Symbol.INTLITERAL ||
			lexer.token == Symbol.FLOATLITERAL) {
			lexer.nextToken();
		}
		else if (lexer.token == Symbol.IDENT) {
			lexer.lookAhead();
			if (lexer.token == Symbol.LPAR) {
				lexer.rollback();
				callExpr();
			}
			else {
				lexer.rollback();
				lexer.nextToken();
			}
		}
		else if (lexer.token == Symbol.LPAR) {
			lexer.nextToken();
			expr();
			if (lexer.token != Symbol.RPAR)
				error.signal("Esperava ')'");
			lexer.nextToken();
		}
		else
			error.signal("Erro sintático");
	}
    
	private Lexer lexer;
    private CompilerError error;

}
    
    
    
