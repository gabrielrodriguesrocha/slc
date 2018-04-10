module Parser where

import Text.Parsec
import Text.Parsec.String (Parser)
import Control.Applicative ((<$>))

import qualified Text.Parsec.Expr as Expr
import qualified Text.Parsec.Token as Token

import Lexer
import Syntax

import Debug.Trace

int :: Parser Expr
int = Int <$> integer

floating :: Parser Expr
floating = Float <$> float

stringVar :: Parser Expr
stringVar = Var <$> stringLiteral

variable :: Parser Expr
variable = Var <$> identifier

--binop = Expr.Infix (BinaryOp <$> op) Expr.AssocLeft

binary s assoc = Expr.Infix (reservedOp s >> return (BinaryOp s)) assoc

op :: Parser String
op = do
  whitespace
  o <- operator
  whitespace
  return o

compop = (reservedOp ">" >> return ">") 
     <|> (reservedOp "<" >> return "<")
     <|> (reservedOp "=" >> return "=")

binops = [[binary "*" Expr.AssocLeft,
    binary "/" Expr.AssocLeft]
  ,[binary "+" Expr.AssocLeft,
    binary "-" Expr.AssocLeft]]

expr :: Parser Expr
expr = Expr.buildExpressionParser binops factor 

comparison :: Parser Comparison -- Problema na geração da AST
comparison = do
  rhs <- expr 
  op <- compop 
  lhs <- expr
  return $ CompOp op rhs lhs

program :: Parser Program
program = do
  reserved "PROGRAM"
  id <- identifier
  reserved "BEGIN"
  decl <- (declaration `endBy` semi) <?> "Declarations"
  body <- (many $ function) 
  reserved "END"
  return $ Program id decl body

function = try voidFunction <|> try floatFunction <|> try intFunction

declaration :: Parser Decl
declaration = try stringDeclaration <|> try intDeclaration <|> floatDeclaration

stringDeclaration :: Parser Decl
stringDeclaration = do
  reserved "STRING"
  id <- identifier <?> "String name"
  reserved ":="
  rhs <- stringVar <?> "String content"
  return $ StringDeclaration id rhs

intDeclaration :: Parser Decl
intDeclaration = do
  reserved "INT"
  id <- commaSep1 identifier
  return $ IntDeclaration id

floatDeclaration :: Parser Decl
floatDeclaration = do
  reserved "FLOAT"
  id <- commaSep1 identifier
  return $ FloatDeclaration id

intParam :: Parser Param
intParam = do
  reserved "INT"
  id <- identifier
  return $ IntParam id

floatParam :: Parser Param
floatParam = do
  reserved "FLOAT"
  id <- identifier
  return $ FloatParam id

param = try intParam <|> try floatParam

voidFunction :: Parser FuncDecl
voidFunction = do
  reserved "FUNCTION"
  reserved "VOID"
  name <- identifier
  args <- parens $ commaSep param
  reserved "BEGIN"
  decl <- (declaration `endBy` semi) <?> "Declarations"
  body <- stmts 
  reserved "END"
  return $ VoidFunction name args decl body

floatFunction :: Parser FuncDecl
floatFunction = do
  reserved "FUNCTION"
  reserved "FLOAT"
  name <- identifier
  args <- parens $ commaSep param
  reserved "BEGIN"
  decl <- (declaration `endBy` semi) <?> "Declarations"
  body <- stmts 
  reserved "END"
  return $ FloatFunction name args decl body

intFunction :: Parser FuncDecl
intFunction = do
  reserved "FUNCTION"
  reserved "INT"
  name <- identifier
  args <- parens $ commaSep param
  reserved "BEGIN"
  decl <- (declaration `endBy` semi) <?> "Declarations"
  body <- stmts 
  reserved "END"
  return $ IntFunction name args decl body

call :: Parser Expr
call = do
  name <- identifier
  args <- parens $ commaSep expr
  return $ Call name args

assignStmt :: Parser Stmt
assignStmt = do
  id <- identifier
  reservedOp ":="
  rhs <- expr
  semi
  return $ Assign id rhs

ifStmt :: Parser Stmt
ifStmt = do
  reserved "IF"
  cond <- parens comparison <?> "Condition"
  reserved "THEN"
  tr <- stmts
  fl <- optionMaybe elsePart
  reserved "ENDIF"
  return $ If cond tr fl

elsePart :: Parser Stmt
elsePart = do
  reserved "ELSE"
  fl <- stmts
  return $ Else fl

forStmt :: Parser Stmt
forStmt = do
  reserved "FOR"
  header <- parens $ do
    start <- assignStmt
    reservedOp ";"
    cond <- comparison <?> "Condition"
    reservedOp ";"
    update <- assignStmt
    return (start, cond, update)
  body <- stmts
  reserved "ENDFOR"
  return $ For header body

readStmt :: Parser Stmt
readStmt = do
  reserved "READ"
  vars <- parens $ commaSep identifier
  reservedOp ";"
  return $ ReadStmt vars

writeStmt :: Parser Stmt
writeStmt = do
  reserved "WRITE"
  vars <- parens $ commaSep identifier
  reservedOp ";"
  return $ WriteStmt vars

returnStmt :: Parser Stmt
returnStmt = do
  reserved "RETURN"
  body <- expr
  reservedOp ";"
  return $ ReturnStmt body

callStmt :: Parser Stmt
callStmt = do
   func <- identifier;
   par <- parens $ commaSep identifier;
   reservedOp ";"
   return $ CallStmt func par


stmt :: Parser Stmt
stmt = try assignStmt
  <|> try readStmt
  <|> try writeStmt
  <|> try returnStmt
  <|> try ifStmt
  <|> try forStmt
  <|> try callStmt

stmts :: Parser [Stmt]
stmts = many stmt

factor :: Parser Expr
factor = try floating
      <|> try int
      <|> try call
      <|> try stringVar
      <|> try variable
      <|> (parens expr)

contents :: Parser a -> Parser a
contents p = do
  Token.whiteSpace lexer
  r <- p
  eof
  return r

toplevel :: Parser [Program]
toplevel = many1 program

parseExpr :: String -> Either ParseError Expr
parseExpr s = parse (contents expr) "<stdin>" s

parseToplevel :: String -> Either ParseError [Program]
parseTopLevel s | trace (show s) False = undefined
parseToplevel s = parse (contents toplevel) "<stdin>" s
