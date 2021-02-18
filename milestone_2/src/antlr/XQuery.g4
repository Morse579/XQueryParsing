grammar XQuery;

@header{
	package antlr;
}


//absolute path	
ap
    : 'doc("' fileName '")' '/' rp 		#ApRoot
	| 'doc("' fileName '")' '//' rp 	#ApCurrent
	;

//relative path
rp
    : tagName				# RpTagName
	| '*'					# RpChildren
	| '.'					# RpCurrent
	| '..'					# RpParent
	| 'text()'				# RpText
	| '@' attName			# RpAttName
	| '(' rp ')'			# RpPathNodes
	| rp '/' rp				# RpRoot
	| rp '//' rp			# RpFromCurr
	| rp '[' filter ']'		# RpFilter
	| rp ',' rp				# RpConcat
	;

//path filter
filter
    : rp						# FilterRp
	| rp '=' rp					# FilterEq
	| rp 'eq' rp				# FilterEq
	| rp '==' rp				# FilterIs
	| rp 'is' rp				# FilterIs
	| rp '=' StringConstant		# FilterEqConst
	| '(' filter ')'			# FilterCurrent
	| filter 'and' filter		# FilterAnd
	| filter 'or' filter		# FilterOr
	| 'not' filter				# FilterNot
	;

xq
    : var                                               # XqVar
    | StringConstant                                    # XqStrConst
    | ap                                                # XqAp
    | '(' xq ')'                                        # XqParenthesis
    | xq ',' xq                                         # XqConcat
    | xq '/' rp                                         # XqDescend
    | xq '//' rp                                        # XqFromCurr
    | '<' tagName '>' '{' xq '}' '</' tagName '>'       # XqNew
    | forClause? letClause? whereClause? returnClause   # XqFLWR
    | letClause xq                                      # XqLet
    ;

forClause
    : 'for' var 'in' xq(',' var 'in' xq)*
    ;

letClause
    : 'let' var ':=' xq(',' var ':=' xq)*
    ;

whereClause
    : 'where' cond
    ;

returnClause
    : 'return' xq
    ;

cond
    : xq '=' xq             # XqCondEq
    | xq 'eq' xq            # XqCondEq
    | xq '==' xq            # XqCondIs
    | xq 'is' xq            # XqCondIs
    | 'empty' '(' xq ')'    # XqCondEmpty
    | 'some' var 'in' xq(',' var 'in' xq)* 'satisfies' cond             # XqCondSatisfy
    | '(' cond ')'          # XqCondXqParenthesis
    | cond 'and' cond       # XqCondAnd
    | cond 'or' cond        # XqCondOr
    | 'not' cond            # XqCondNot
    ;


	
/*TOKENS*/

var:      '$' tagName;
tagName:  ID;
attName:  ID;

ID: [a-zA-Z0-9_-]+ ;

fileName: FILENAME;
FILENAME: (Letter | Digit | '_' | '.')*; 
//tagName : (Letter | Digit | '_' | '.')*;  
//attName : (Letter | Digit | '_' | '.')*;

StringConstant : '"' [_A-Za-z0-9.!, ?:;]* '"';
	
Letter: [a-zA-Z];
Digit: [0-9];

WS: [ \n\t\r]+ -> skip;
	
	

