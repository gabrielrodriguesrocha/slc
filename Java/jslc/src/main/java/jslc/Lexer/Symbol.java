package jslc.Lexer;
//modificado
public enum Symbol {

	EOF("eof"),
	IDENT("Ident"),
	NUMBER("Number"),
	PROGRAM("PROGRAM"),
	BEGIN("BEGIN"),
	END("END"),
	FUNCTION("FUNCTION"),
	READ("READ"),
	WRITE("WRITE"),
	IF("IF"),
	THEN("THEN"),
	ELSE("ELSE"),
	ENDIF("ENDIF"),
	RETURN("RETURN"),
	FOR("FOR"),
	ENDFOR("ENDFOR"),
	FLOAT("FLOAT"),
	INT("INT"),
	VOID("VOID"),
	STRING("STRING"),
	PLUS("+"),
	MINUS("-"),
	MULT("*"),
	DIV("/"),
	EQUAL("="),
	LT("<"),
	GT(">"),
	LPAR("("),
	RPAR(")"),
	ASSIGN(":="),
	COMMA(","),
	SEMICOLON(";"),
	INTLITERAL("IntNumber"),
	FLOATLITERAL("FloatNumber"),
	STRINGLITERAL("StringLiteral");

	Symbol(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private String name;

}
