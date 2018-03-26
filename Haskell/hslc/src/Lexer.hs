module Lexer where

import Text.Parsec.String (Parser)
import Text.Parsec.Language (emptyDef)
import Text.Parsec.Prim (many)

import qualified Text.Parsec.Token as Token

lexer :: Token.TokenParser()
lexer = Token.makeTokenParser style
    where
        ops = [":=", "+", "-", "*", "/", "=", "<", ">", "(", ")", ";", ","]
        names = ["PROGRAM", "BEGIN", "END", "PROTO", "FUNCTION", "READ", "WRITE",
                 "IF", "THEN", "ELSE", "ENDIF", "RETURN", "FOR", "ENDFOR",
                 "FLOAT", "INT", "VOID", "STRING"]
        style = emptyDef {
            Token.commentLine = "--",
            Token.reservedOpNames = ops,
            Token.reservedNames = names,
            Token.caseSensitive = True
        }

integer = Token.integer lexer
float = Token.float lexer
stringLiteral = Token.stringLiteral lexer
parens = Token.parens lexer
commaSep1 = Token.commaSep1 lexer
semiSep1 = Token.semiSep1 lexer
commaSep = Token.commaSep lexer
semiSep = Token.semiSep lexer
identifier = Token.identifier lexer
whitespace = Token.whiteSpace lexer
reserved = Token.reserved lexer
reservedOp = Token.reservedOp lexer

semi = Token.semi lexer
comma = Token.comma lexer

operator :: Parser String
operator = do
  c <- Token.opStart emptyDef
  cs <- many $ Token.opLetter emptyDef
  return (c:cs)