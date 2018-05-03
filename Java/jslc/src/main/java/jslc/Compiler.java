package jslc;

import jslc.Lexer.*;

import jslc.Error.*;

import jslc.AST.*;

import java.util.*;

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

    public Program compile( char []p_input ) {
		error = new CompilerError(null);
        lexer = new Lexer(p_input, error);
		error.setLexer(lexer);
        lexer.nextToken();
		Program p = program();
		if (error.wasAnErrorSignalled())
            return null;
		return p;
    }
    
    // Program ::= Decl FuncDecl
    public Program program(){
		ArrayList <Decl> d = new ArrayList<Decl>();
		ArrayList <FuncDecl> f = new ArrayList<FuncDecl>();
        if (lexer.token != Symbol.PROGRAM)
            error.signal("Esperava PROGRAM");
        lexer.nextToken();
        if (lexer.token != Symbol.IDENT)
            error.signal("Esperava nome do programa");
        lexer.nextToken();
        if (lexer.token != Symbol.BEGIN)
            error.signal("Esperava BEGIN");
		lexer.nextToken();
		if (isVarType() || lexer.token == Symbol.STRING)
			d = decl();
		if (lexer.token == Symbol.FUNCTION)
        	f = funcDecl();
        if (lexer.token != Symbol.END)
			error.signal("Esperava declaração de variável, função ou END");
		return new Program(d, f);
    }

    // Decl ::= VarDeclList {Decl} | StringDeclList {Decl} | empty
    public ArrayList<Decl> decl() {
		StringDecl stringD;
		VarDecl varD;
		ArrayList<Decl> decls = new ArrayList<Decl>();

        while (lexer.token == Symbol.STRING || isVarType()) {
			if (lexer.token == Symbol.STRING) {
				stringD = stringDecl();
				decls.add(stringD);
            }
            else if (isVarType()) { //perhaps refactor
				varD = varDecl();
				decls.add(varD);
			}
		 }
		 if (!isVarType() &&
			  lexer.token != Symbol.STRING &&
			  lexer.token != Symbol.FUNCTION &&
			  !statementSymbol()) {
		 	error.signal("Tipo de variável não suportado, esperava STRING, INT ou FLOAT");
		}
		 
		 return decls;
    }

	// VarType ::= FLOAT | INT
    public boolean isVarType() {
		return lexer.token == Symbol.FLOAT || lexer.token == Symbol.INT;
    }
       
	// VarDecList ::= VarType IdList ; | empty
	public VarDecl varDecl(){
		Type type;
		ArrayList<String> vars;

		type = varType();
        vars = idList(true);
		if (lexer.token != Symbol.SEMICOLON)
            error.signal("Esperava ';'");
		lexer.nextToken();
		
		return new VarDecl(type, vars);
    }
	
	// IdList ::= Id [, IdList]* | Id
    public ArrayList <String> idList(boolean declaration) {
		ArrayList <String> vars =  new ArrayList<String>();
        while (lexer.token == Symbol.IDENT) {
			if (declaration && vars.contains(lexer.getStringValue()) {
				error.signal("Variável " + lexer.getStringValue() + " já declarada.");
			}
			vars.add(lexer.getStringValue());
            lexer.nextToken();
            if (lexer.token != Symbol.COMMA)
                return vars;
            lexer.nextToken();
        }
		error.signal("Não é um identificador válido");
		return null;
    }
    
	// StringDeclList ::= [STRING id := STRINGLITERAL ;]*
    public StringDecl stringDecl(){
		String id, val;

        lexer.nextToken();
        if (lexer.token != Symbol.IDENT)
			error.signal("Declaração de variável com palavra reservada");
		id = lexer.getStringValue();
        lexer.nextToken();
        if (lexer.token != Symbol.ASSIGN)
            error.signal("Esperava ':=''");
        lexer.nextToken();
		if (lexer.token != Symbol.STRINGLITERAL)
			error.signal("Esperava string literal");
		val = lexer.getStringValue();
        lexer.nextToken();
        if (lexer.token != Symbol.SEMICOLON)
            error.signal("Esperava ;");
		lexer.nextToken();

		return new StringDecl(Type.stringType, id, val);
    }

	// AnyType ::= VarType | VOID
    public boolean isAnyType() {
		if (isVarType() || lexer.token == Symbol.VOID)
			return true;
		else
			error.signal("Tipo de função não suportado, esperava INT, FLOAT ou VOID");
        return false;
    }
    
	// FuncDecl ::= [FUNCTION AnyType ([ParamDeclList]*) BEGIN FuncBody END]*
    public ArrayList<FuncDecl> funcDecl() {
		Type type;
		String id;
		ArrayList<Param> params = new ArrayList<Param>();
		ArrayList<Decl> decls = new ArrayList<Decl>();
		ArrayList<Stmt> stmts = new ArrayList<Stmt>();
		ArrayList<FuncDecl> functions = new ArrayList<FuncDecl>();

        while(lexer.token == Symbol.FUNCTION) {
            lexer.nextToken();
			type = anyType();
            if (lexer.token != Symbol.IDENT)
				error.signal("Esperava identificador");
			id = lexer.getStringValue();
            lexer.nextToken();
		    if (lexer.token != Symbol.LPAR)
				error.signal("Esperava '('");
			lexer.nextToken();
			params = paramDeclList();
			if (lexer.token != Symbol.RPAR)
				error.signal("Esperava ')'");
			lexer.nextToken();
			if (lexer.token != Symbol.BEGIN)
				error.signal("Esperava begin");
			lexer.nextToken();
			if (isVarType() || lexer.token == Symbol.STRING)
				decls = decl();
			if (statementSymbol())
				stmts = stmtList();
			if (lexer.token != Symbol.END)
				error.signal("Esperava end");
			lexer.nextToken();
			functions.add(new FuncDecl (type, id, params, decls, stmts));
		}
		return functions;
    }

	// ParamDeclList ::= [VarType Id ,]* | VarType Id
	public ArrayList<Param> paramDeclList() {
		Type type;
		String id;
		ArrayList<Param> params = new ArrayList<Param>();

		while (isVarType()) {
			type = varType();
			if (lexer.token != Symbol.IDENT)
				error.signal("Esperava identificador");
			id = lexer.getStringValue();
			lexer.nextToken();
			params.add(new Param(type, id));
			if (lexer.token != Symbol.COMMA)
				break;
			lexer.nextToken();
		}
		return params;
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
	public ArrayList<Stmt> stmtList() {
		ArrayList<Stmt> stmts = new ArrayList<Stmt>();

		while (statementSymbol()) {
			if (lexer.token == Symbol.IDENT) {
				lexer.lookAhead(); // danger lies in breaking patterns
				if (lexer.token == Symbol.ASSIGN) {
					lexer.rollback();
					stmts.add(assignStmt());
				}
				else if (lexer.token == Symbol.LPAR) {
					lexer.rollback();
					stmts.add(callStmt());
				}
				else if (lexer.token == Symbol.EQUAL) {
					error.signal("Esperava := ao invés de =");
				}
				else
					error.signal("Esperava atribuição ou chamada de função");
			}
			else if (lexer.token == Symbol.READ) {
				stmts.add(readStmt());
			}
			else if (lexer.token == Symbol.WRITE) {
				stmts.add(writeStmt());
			}
			else if	(lexer.token == Symbol.RETURN) {
				stmts.add(returnStmt());
			}
			else if (lexer.token == Symbol.IF) {
				stmts.add(ifStmt());
			}
			else if (lexer.token == Symbol.FOR) {
				stmts.add(forStmt());
			}
		}

		return stmts;
	}

	// AssignStmt ::= AssignExpr ;
	public AssignStmt assignStmt() {
		AssignExpr e = assignExpr();
		//lexer.nextToken();
		if(lexer.token != Symbol.SEMICOLON)
			error.signal ("Esperava ';'");
		lexer.nextToken();
		return new AssignStmt(e);
	}

	// CallStmt ::= CallExpr ;
	public CallStmt callStmt() {
		CallExpr c = callExpr();
		if(lexer.token != Symbol.SEMICOLON)
			error.signal ("Esperava ';'");
		lexer.nextToken();
		return new CallStmt(c);
	}
	
	// CallExpr ::= Id ( ExprList )
	public CallExpr callExpr() {
		String id;
		ArrayList<Expr> exprs = new ArrayList<Expr>();
		
		if (lexer.token != Symbol.IDENT)
			error.signal("Esperava identificador");
		id = lexer.getStringValue();
		lexer.nextToken();
		if (lexer.token != Symbol.LPAR)
			error.signal ("Esperava '('");
		lexer.nextToken();
		exprs = exprList();
		if(lexer.token != Symbol.RPAR)
			error.signal ("Esperava ')'");
		lexer.nextToken();
		return new CallExpr(id, exprs);
	}
	
	// ReadStmt ::= READ ( IdList ) ;
	public ReadStmt readStmt() {
		ArrayList<String> ids;
		
		if (lexer.token != Symbol.READ)
			error.signal("Esperava read");
		lexer.nextToken();
		if (lexer.token != Symbol.LPAR)
			error.signal ("Esperava '('");
		lexer.nextToken();
		ids = idList(false);
		if(lexer.token != Symbol.RPAR)
			error.signal ("Esperava ')'");
		lexer.nextToken();
		if(lexer.token != Symbol.SEMICOLON)
			error.signal ("Esperava ';'");
		lexer.nextToken();

		return new ReadStmt(ids);
	}
	
	// ReadStmt ::= WRITE ( IdList ) ;
	public WriteStmt writeStmt() {
		ArrayList<String> ids;

		if (lexer.token != Symbol.WRITE)
			error.signal("Esperava read");
		lexer.nextToken();
		if (lexer.token != Symbol.LPAR)
			error.signal ("Esperava '('");
		lexer.nextToken();
		ids = idList(false);
		if(lexer.token != Symbol.RPAR)
			error.signal ("Esperava ')'");
		lexer.nextToken();
		if(lexer.token != Symbol.SEMICOLON)
			error.signal ("Esperava ';'");
		lexer.nextToken();

		return new WriteStmt(ids);
	}

	// ReturnStmt ::= RETURN Expr;
	public ReturnStmt returnStmt() {
		if (lexer.token != Symbol.RETURN)
			error.signal("Esperava return");
		lexer.nextToken();
		Expr e = expr();
		if (lexer.token != Symbol.SEMICOLON)
			error.signal("Esperava ';'");
		lexer.nextToken();
		return new ReturnStmt(e);
	}

	// IfStmt ::= IF ( CompExpr ) THEN StmtList {ElseStmt} ENDIF
	public IfStmt ifStmt() {
		CompExpr cond;
		ArrayList<Stmt> stmts;
		ElseStmt elsePart = null;

		if (lexer.token != Symbol.IF)
			error.signal("Esperava if");
		lexer.nextToken();
		if (lexer.token != Symbol.LPAR)
			error.signal("Esperava '('");
		lexer.nextToken();
		cond = compExpr();
		if (lexer.token != Symbol.RPAR)
			error.signal("Esperava ')'");
		lexer.nextToken();
		if (lexer.token != Symbol.THEN)
			error.signal("Esperava then");
		lexer.nextToken();
		stmts = stmtList();
		if (lexer.token == Symbol.ELSE) {
			elsePart = elseStmt();
		}
		else if (lexer.token != Symbol.ENDIF) {
			error.signal("Esperava endif");	
		}
		lexer.nextToken();

		return new IfStmt(cond, stmts, elsePart);
	}

	// ElseStmt ::= ELSE StmtList
	public ElseStmt elseStmt() {
		ArrayList<Stmt> stmts = new ArrayList<Stmt>();

		if (lexer.token != Symbol.ELSE)
			error.signal("Esperava else");
		lexer.nextToken();
		stmts = stmtList();

		return new ElseStmt(stmts);
	}

	// CompExpr ::= Expr CompOp Expr
	public CompExpr compExpr () {
		Expr l, r;
		Symbol op;

		l = expr();
		//lexer.nextToken();
		if (!compop())
			error.signal("Esperava >, < ou =");
		op = lexer.token;
		lexer.nextToken();
		r = expr();

		return new CompExpr(l, op, r);
	}

	// CompOp ::= > | < | =
	public boolean compop () {
		return lexer.token == Symbol.GT || lexer.token == Symbol.LT || lexer.token == Symbol.EQUAL;
	}

	// ForStmt ::= FOR ({AssignExpr} ; {CompExpr} ; {AssignExpr}) StmtList ENDFOR
	public ForStmt forStmt() {
		AssignExpr init, step;
		CompExpr cond;
		ArrayList<Stmt> stmts;

		if (lexer.token != Symbol.FOR)
			error.signal("Esperava for");
		lexer.nextToken();
		if (lexer.token != Symbol.LPAR)
			error.signal("Esperava '(");
		lexer.nextToken();
		init = assignExpr();
		if (lexer.token != Symbol.SEMICOLON)
			error.signal("Esperava ';'");
		lexer.nextToken();
		cond = compExpr();
		if (lexer.token != Symbol.SEMICOLON)
			error.signal("Esperava ';'");
		lexer.nextToken();
		step = assignExpr();
		if (lexer.token != Symbol.RPAR)
			error.signal("Esperava ')'");
		lexer.nextToken();
		stmts = stmtList();
		if (lexer.token != Symbol.ENDFOR)
			error.signal("Esperava endfor");
		lexer.nextToken();

		return new ForStmt(init, cond, step, stmts);
	}

	// AssignExpr ::= Id := Expr
	public AssignExpr assignExpr() {
		String id;
		Expr e;

		if (lexer.token != Symbol.IDENT)
			error.signal("Esperava identificador");
		id = lexer.getStringValue();
		lexer.nextToken();
		if (lexer.token != Symbol.ASSIGN)
			error.signal("Esperava :=");
		lexer.nextToken();
		e = expr();	

		return new AssignExpr(id, e);
	}
	
    // ExprList ::= [Expr]+ 
	public ArrayList<Expr> exprList() {
		ArrayList<Expr> exprs = new ArrayList<Expr>();

		if (lexer.token != Symbol.IDENT &&
			lexer.token != Symbol.LPAR &&
			lexer.token != Symbol.RPAR &&
			lexer.token != Symbol.FLOATLITERAL &&
			lexer.token != Symbol.INTLITERAL
			)
			error.signal("Esperava expressão");
	
		while(lexer.token == Symbol.IDENT ||
			  lexer.token == Symbol.LPAR ||
			  lexer.token == Symbol.FLOATLITERAL ||
			  lexer.token == Symbol.INTLITERAL) {
			exprs.add(expr());
			if (lexer.token != Symbol.COMMA)
				break;
			lexer.nextToken();
		}

		return exprs;
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
	public Expr expr() {
		Factor f,g;
		Symbol op;
		ArrayList <ExprTail> tail = new ArrayList<ExprTail>();

		f = factor();
		while (addop()) {
			op = lexer.token;
			lexer.nextToken();
			g = factor();
			tail.add(new ExprTail(op, g));
		}

		return new Expr(f, tail);
	}

	// Factor ::= PostfixExpr [MulOp PostfixExpr]*
	public Factor factor() {
		PostfixExpr p,q;
		Symbol op;
		ArrayList <FactorTail> tail = new ArrayList<FactorTail>();

		p = postfixExpr();
		while (mulop()) {
			op = lexer.token;
			lexer.nextToken();
			q = postfixExpr();
			tail.add(new FactorTail(op, q));
		}

		return new Factor(p, tail);
	}

	// PostfixExpr ::= INTLITERAL | FLOATLITERAL | Id | ( Expr ) | CallExpr
	public PostfixExpr postfixExpr() {
		if (lexer.token == Symbol.INTLITERAL) {
			int val = lexer.getIntValue();
			lexer.nextToken();
			return new IntT(val);
		}
		else if (lexer.token == Symbol.FLOATLITERAL) {
			float val = lexer.getFloatValue();
			lexer.nextToken();
			return new FloatT(val);
		}
		else if (lexer.token == Symbol.IDENT) {
			lexer.lookAhead();
			if (lexer.token == Symbol.LPAR) {
				lexer.rollback();
				return callExpr();
			}
			else {
				lexer.rollback();
				lexer.nextToken();
				return new Identifier(lexer.getStringValue());
			}
		}
		else if (lexer.token == Symbol.LPAR) {
			lexer.nextToken();
			Expr e = expr();
			if (lexer.token != Symbol.RPAR)
				error.signal("Esperava ')'");
			lexer.nextToken();
			return new PostfixExpr(e);
		}
		else {
			error.signal("Erro sintático");
			return null;
		}
	}

	// varType ::= INT | FLOAT | STRING
	private Type varType() {
		Type result = null;
		
		if (!isVarType()) {
			error.signal("Esperava INT ou FLOAT");
		}

		if (lexer.token == Symbol.INT) {
			result = Type.intType;
		}
		else if (lexer.token == Symbol.FLOAT) {
			result = Type.floatType;
		}

		lexer.nextToken();
		return result;
	}

	private Type anyType() {
		Type result;

		isAnyType();

		if (isVarType()) {
			result = varType();
			return result;
		}
		else {
			result = Type.voidType;
			lexer.nextToken();
			return result;
		}
	}
    
	private Lexer lexer;
    private CompilerError error;
	private SymbolTable sTable;

}
    
    
    
