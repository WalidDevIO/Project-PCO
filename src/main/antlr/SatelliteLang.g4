grammar SatelliteLang;

@header {
package simulation.antlr4;
}

// --- Règle de départ ---
program
    : statement* EOF
    ;

// --- Instructions possibles ---
statement
    : assignStmt ';'                #assignStatement
    | methodCall ';'                #methodCallStatement
    | COMMENT                       #commentStatement
    ;

// --- Affectation : var := new ClassName(params) ---
assignStmt
    : ID ':=' instantiation
    ;

// --- Appel méthode : var.method() ---
methodCall
    : ID '.' ID '(' argList? ')'
    ;

// --- Liste d'arguments : a = 10, b = "test", c = new Class() ---
argList
    : arg (',' arg)*
    ;

arg
    : ID '=' expr
    ;

// --- Expressions simples et instanciations ---
expr
    : NUMBER
    | STRING
    | HASHWORD
    | ID
    | instantiation
    ;

// --- Instanciation : new ClassName(params) ---
instantiation
    : 'new' ID '(' argList? ')'
    ;

// --- Lexèmes ---
ID        : [a-zA-Z_][a-zA-Z_0-9]* ;
NUMBER    : [0-9]+ ('.' [0-9]+)? ;
STRING    : '"' (~["\\] | '\\' .)* '"' ;
HASHWORD  : '#' [a-zA-Z_][a-zA-Z_0-9]* ;
COMMENT   : '//' ~[\r\n]* -> skip ;
WS        : [ \t\r\n]+ -> skip ;
