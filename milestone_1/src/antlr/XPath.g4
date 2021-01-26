grammar XPath;

@header{
	package antlr;
}

//absolute path	
ap:
	  doc(fileName) '/' rp 		#ApRoot
	| doc(fileName) '//' rp 	#ApCurrent
	;

//relative path
rp:
	  tagName			 # RpTagName
	| '*'         		 # RpChildren
	| '.'     			 # RpCurrent
	| '..'    			 # RpParent
	| 'text()'  		 # RpText
	| '@' attName 		 # RpAttName
	| '(' rp ')'         # Rp
	| rp '/' rp          # Rp
	| rp '//' rp         # Rp
	| rp '[' filter ']'  # Rp
	| rp ',' rp          # Rp
	;

//path filter
filter:
	  rp
	| rp '=' rp 				# FilterEq
	| rp 'eq' rp 				# FilterEq
	| rp '==' rp 				# FilterIs
	| rp 'is' rp 				# FilterIs
	| rp '=' StringConstant 	# FilterEqConst
	| '(' filter ')' 			# FilterParentheses
	| filter 'and' filter 		# FilterAnd
	| filter 'or' filter 		# FilterOr
	| 'not' filter 				# FilterNot
	;
	
/*TOKENS*/
fileName : (Letter | Digit | '_' | '.')*  
tagName : (Letter | Digit | '_' | '.')*  
attName : (Letter | Digit | '_' | '.')*  
StringConstant : 
	
Letter: [a-zA-Z];
Digit: [0-9];

WS: [ \n\t\r]+ -> skip;
	
	

