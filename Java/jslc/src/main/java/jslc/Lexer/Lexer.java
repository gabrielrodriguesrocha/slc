
package jslc.Lexer;

import java.util.*;
import jslc.Error.*;

public class Lexer {

	// apenas para verificacao lexica
    public static final boolean DEBUGLEXER = false;
    public static boolean LOOKAHEADCALL = false;
    
    public Lexer( char []input, CompilerError error ) {
        this.input = input;
        // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
        // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        this.error = error;
    }
    
    // contains the keywords
    static private Hashtable<String, Symbol> keywordsTable;
    
    // this code will be executed only once for each program execution
    static {
        keywordsTable = new Hashtable<String, Symbol>();
        keywordsTable.put( "PROGRAM", Symbol.PROGRAM );
        keywordsTable.put( "BEGIN", Symbol.BEGIN );
        keywordsTable.put( "END", Symbol.END );
        keywordsTable.put( "FUNCTION", Symbol.FUNCTION );
        keywordsTable.put( "READ", Symbol.READ );
        keywordsTable.put( "WRITE", Symbol.WRITE );
        keywordsTable.put( "IF", Symbol.IF );
        keywordsTable.put( "THEN", Symbol.THEN );
        keywordsTable.put( "ELSE", Symbol.ELSE );
        keywordsTable.put( "ENDIF", Symbol.ENDIF );
        keywordsTable.put( "RETURN", Symbol.RETURN );
        keywordsTable.put( "FOR", Symbol.FOR );
        keywordsTable.put( "ENDFOR", Symbol.ENDFOR );
        keywordsTable.put( "FLOAT", Symbol.FLOAT );
        keywordsTable.put( "INT", Symbol.INT);
        keywordsTable.put( "VOID", Symbol.VOID );
        keywordsTable.put( "STRING", Symbol.STRING );
    }
    
    
    public void nextToken() {
        while(input[tokenPos] == ' ' ||
              input[tokenPos] == '\n' || 
              input[tokenPos] == '\t' ||
              input[tokenPos] == '\r' ){
            
            if (input[tokenPos] == '\n'){
                lineNumber++;
            }
            tokenPos++;
        }

        if (input[tokenPos] == '\0'){
        token = Symbol.EOF;
        return;
    }

    if (input[tokenPos] == '-' && input[tokenPos+1] == '-'){
        while(input[tokenPos] != '\n' && input[tokenPos] != '\0'){
            tokenPos++;
        }
        nextToken(); // Reinicia na linha seguinte
        return;
    }

    String aux = "";

    if (input[tokenPos] == '.') { // Floats sem unidade
        aux += input[tokenPos];
        tokenPos++;
        isFloat = true;
    }

    while (Character.isDigit(input[tokenPos])) {
        aux += input[tokenPos];
        tokenPos++;
        if (input[tokenPos] == '.') {
            isFloat = true;
			tokenPos++;
        }
    }
    if(!aux.equals("")) {
        if (isFloat == true && aux.length() > 1) {
            floatValue = Float.parseFloat(aux);
            if (floatValue > Float.MAX_VALUE) {
                error.signal("Número superior ao máximo habilitado");
            }
            else if (floatValue < - Float.MIN_VALUE){
                error.signal("Número inferior ao mínimo habilitado");
            }
            token = Symbol.FLOATLITERAL;
        }
        else if (aux.length() >= 1) {
            intValue = Integer.parseInt(aux);
            if (intValue > Integer.MAX_VALUE) {
                error.signal("Número superior ao máximo habilitado");
            }
            else if (intValue < Integer.MIN_VALUE) {
                error.signal("Número inferior ao mínimo habilitado");
            }
            token = Symbol.INTLITERAL;
        }
    }
    else {
		tokenLen = 0;
        if (input[tokenPos] == '"') {
            while(input[tokenPos+1] != '"') {
				tokenPos++;
                aux += input[tokenPos];
				tokenLen++;
            } 
			if (tokenLen > MaxStringLength) {
				error.signal("String de comprimento superior ao máximo permitido");
			}
			tokenPos = tokenPos + 2; // ignores quotation marks
            token = Symbol.STRINGLITERAL;
            stringValue = aux;
        }
        else {
            while (Character.isLetter(input[tokenPos]) ||
				   Character.isDigit(input[tokenPos])) {
                aux += input[tokenPos];
                tokenPos++;
				tokenLen++;
            }
            if (!aux.equals("")) {
                token = keywordsTable.get(aux);
                if (token == null) {
                    if (tokenLen <= MaxIdentifierLength) {
                        token = Symbol.IDENT;
                        stringValue = aux;
                    }
                    else
					    error.signal("Identificador de comprimento superior ao máximo permitido.");
                }
            }
            else {
                switch (input[tokenPos]) {
                    case '+':
                            token = Symbol.PLUS;
                            break;
                    case '-':
                            token = Symbol.MINUS;
                            break;
                    case '/':
                            token = Symbol.DIV;
                            break;
                    case '*':
                            token = Symbol.MULT;
                            break;
                    case '<':
                            token = Symbol.LT;
                            break;
                    case '>':
                            token = Symbol.GT;
                            break;
                    case ':' :
                            if (input[tokenPos+1] == '=') {
                                token = Symbol.ASSIGN;
                                tokenPos++;
                            }
                            break;
                    case '=':
                            token = Symbol.EQUAL;
                            break;
                    case ',':
                            token = Symbol.COMMA;
                            break;
                    case ';':
                            token = Symbol.SEMICOLON;
                            break;
                    case '(' :
                            token = Symbol.LPAR;
                            break;
                    case ')' :
                            token = Symbol.RPAR;
                            break;
                    default:
                            error.signal("Erro léxico");
                }	
                tokenPos++;
            }
        }
    }


		if (DEBUGLEXER && !LOOKAHEADCALL) {
            System.out.println(token.toString());
        }
            
        if (token == Symbol.STRINGLITERAL ||
            token == Symbol.INTLITERAL ||
            token == Symbol.FLOATLITERAL ||
            token == Symbol.IDENT) {
            lastTokenPos = tokenPos - aux.length();
        }
        else
            lastTokenPos = tokenPos - token.toString().length();
    }

    // making an LL(1) parser has never been easier with these incredible functions!
	public void lookAhead() {
        //if (DEBUGLEXER)
        //    System.out.println("Lookahead: ");
        lookahead = token;
        LOOKAHEADCALL = true;
        nextToken();
        LOOKAHEADCALL = false;
	}

	public void rollback() {
        token = lookahead;
        tokenPos = lastTokenPos;
	}
    
    // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }
    
    public String getCurrentLine() {
        int i = lastTokenPos;
        if ( i == 0 )
            i = 1;
        else
            if ( i >= input.length )
                i = input.length;
        
        StringBuffer line = new StringBuffer();
        // go to the beginning of the line
        while ( i >= 1 && input[i] != '\n' )
            i--;
        if ( input[i] == '\n' )
            i++;
        // go to the end of the line putting it in variable line
        while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
            line.append( input[i] );
            i++;
        }
        return line.toString();
    }
    
    public String getStringValue() {
        return stringValue;
    }
    
    public int getIntValue() {
        return intValue;
    }
    
    public float getFloatValue() {
        return floatValue;
    }
    
	public char getCharValue() {
        return charValue;
    }
    // current token
    public Symbol token;
	public Symbol lookahead;
    private String stringValue;
    private int intValue;
    private float floatValue;
    private char charValue;
    private boolean isFloat;
    
    private int  tokenPos;
	private int  tokenLen;
    //  input[lastTokenPos] is the last character of the last token
    private int lastTokenPos;
    // program given as input - source code
    private char []input;
    
    // number of current line. Starts with 1
    private int lineNumber;
    
    private CompilerError error;
    private static final int MaxValueInteger = 32768;
	private static final int MaxStringLength = 80;
	private static final int MaxIdentifierLength = 30;
}
