package jslc.Lexer;

public enum Symbol {

	EOF("eof"),
	IDENT("Ident"),
	NUMBER("Number"),
	PROGRAM("program"),
	BEGIN("begin"),
	END("end"),
	FUNCTION("function"),
	READ("read"),
	WRITE("write"),
	IF("if"),
	THEN("then"),
	ELSE("else"),
	ENDIF("endif"),
	RETURN("return"),
	FOR("for"),
	ENDFOR("endfor"),
	FLOAT("float"),
	INT("int"),
	VOID("void"),
	STRING("string"),
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
