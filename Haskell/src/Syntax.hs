module Syntax where

type Name = String

data Expr
    = Int Integer
    | Float Double
    | Var String
    | Call Name [Expr]
    | BinaryOp Name Expr Expr
    | CompOp Name Expr Expr
    deriving (Eq, Ord, Show)

data Stmt
    = Assign Name Expr
    | If Expr [Stmt] (Maybe Stmt)
    | Else [Stmt]
    | For (Stmt, Expr, Stmt) [Stmt]
    | ReadStmt [Name]
    | WriteStmt [Name]
    | ReturnStmt Expr
    deriving (Eq, Ord, Show)

data Decl
    = StringDeclaration Name Expr
    | IntDeclaration [Name]
    | FloatDeclaration [Name]
    deriving (Eq, Ord, Show)

data FuncDecl
    = VoidFunction Name [Param] [Decl] [Stmt]
    | IntFunction Name [Param] [Decl] [Stmt]
    | FloatFunction Name [Param] [Decl] [Stmt]
    deriving (Eq, Ord, Show)

data Param
    = IntParam Name
    | FloatParam Name
    deriving (Eq, Ord, Show)

data Program
    = Program Name [Decl] [FuncDecl]
    deriving (Eq, Ord, Show)
