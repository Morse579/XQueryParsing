grammar XPath;

@header{
	package antlr;
}

//absolute path	
ap:
	  'doc("' fileName '")' '/' rp 		#ApRoot
	| 'doc("' fileName '")' '//' rp 	#ApCurrent
	;

//relative path
rp:
	  tagName				# RpTagName
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
filter:
	  rp						# FilterRp
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
	
/*TOKENS*/
fileName : (Letter | Digit | '_' | '.')*;  
tagName : (Letter | Digit | '_' | '.')*;  
attName : (Letter | Digit | '_' | '.')*;  
StringConstant : '"' [_A-Za-z0-9.!, ?:;]* '"';
	
Letter: [a-zA-Z];
Digit: [0-9];

WS: [ \n\t\r]+ -> skip;
	
	

