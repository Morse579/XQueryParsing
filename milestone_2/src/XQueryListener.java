// Generated from antlr/XQuery.g4 by ANTLR 4.9.1

	package antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XQueryParser}.
 */
public interface XQueryListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code ApRoot}
	 * labeled alternative in {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 */
	void enterApRoot(XQueryParser.ApRootContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ApRoot}
	 * labeled alternative in {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 */
	void exitApRoot(XQueryParser.ApRootContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ApCurrent}
	 * labeled alternative in {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 */
	void enterApCurrent(XQueryParser.ApCurrentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ApCurrent}
	 * labeled alternative in {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 */
	void exitApCurrent(XQueryParser.ApCurrentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpParent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpParent(XQueryParser.RpParentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpParent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpParent(XQueryParser.RpParentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpAttName}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpAttName(XQueryParser.RpAttNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpAttName}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpAttName(XQueryParser.RpAttNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpFromCurr}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpFromCurr(XQueryParser.RpFromCurrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpFromCurr}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpFromCurr(XQueryParser.RpFromCurrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpText}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpText(XQueryParser.RpTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpText}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpText(XQueryParser.RpTextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpTagName}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpTagName(XQueryParser.RpTagNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpTagName}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpTagName(XQueryParser.RpTagNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpChildren}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpChildren(XQueryParser.RpChildrenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpChildren}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpChildren(XQueryParser.RpChildrenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpCurrent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpCurrent(XQueryParser.RpCurrentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpCurrent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpCurrent(XQueryParser.RpCurrentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpConcat}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpConcat(XQueryParser.RpConcatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpConcat}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpConcat(XQueryParser.RpConcatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpRoot}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpRoot(XQueryParser.RpRootContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpRoot}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpRoot(XQueryParser.RpRootContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpFilter}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpFilter(XQueryParser.RpFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpFilter}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpFilter(XQueryParser.RpFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpPathNodes}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpPathNodes(XQueryParser.RpPathNodesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpPathNodes}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpPathNodes(XQueryParser.RpPathNodesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilterEqConst}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilterEqConst(XQueryParser.FilterEqConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilterEqConst}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilterEqConst(XQueryParser.FilterEqConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilterNot}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilterNot(XQueryParser.FilterNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilterNot}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilterNot(XQueryParser.FilterNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilterOr}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilterOr(XQueryParser.FilterOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilterOr}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilterOr(XQueryParser.FilterOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilterAnd}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilterAnd(XQueryParser.FilterAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilterAnd}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilterAnd(XQueryParser.FilterAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilterRp}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilterRp(XQueryParser.FilterRpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilterRp}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilterRp(XQueryParser.FilterRpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilterEq}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilterEq(XQueryParser.FilterEqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilterEq}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilterEq(XQueryParser.FilterEqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilterIs}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilterIs(XQueryParser.FilterIsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilterIs}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilterIs(XQueryParser.FilterIsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilterCurrent}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilterCurrent(XQueryParser.FilterCurrentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilterCurrent}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilterCurrent(XQueryParser.FilterCurrentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqAp}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqAp(XQueryParser.XqApContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqAp}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqAp(XQueryParser.XqApContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqVar}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqVar(XQueryParser.XqVarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqVar}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqVar(XQueryParser.XqVarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqFromCurr}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqFromCurr(XQueryParser.XqFromCurrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqFromCurr}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqFromCurr(XQueryParser.XqFromCurrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqParenthesis}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqParenthesis(XQueryParser.XqParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqParenthesis}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqParenthesis(XQueryParser.XqParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqJoin}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqJoin(XQueryParser.XqJoinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqJoin}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqJoin(XQueryParser.XqJoinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqConcat}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqConcat(XQueryParser.XqConcatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqConcat}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqConcat(XQueryParser.XqConcatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqFLWR}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqFLWR(XQueryParser.XqFLWRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqFLWR}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqFLWR(XQueryParser.XqFLWRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqNew}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqNew(XQueryParser.XqNewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqNew}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqNew(XQueryParser.XqNewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqStrConst}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqStrConst(XQueryParser.XqStrConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqStrConst}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqStrConst(XQueryParser.XqStrConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqLet}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqLet(XQueryParser.XqLetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqLet}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqLet(XQueryParser.XqLetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqDescend}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqDescend(XQueryParser.XqDescendContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqDescend}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqDescend(XQueryParser.XqDescendContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#forClause}.
	 * @param ctx the parse tree
	 */
	void enterForClause(XQueryParser.ForClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#forClause}.
	 * @param ctx the parse tree
	 */
	void exitForClause(XQueryParser.ForClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#letClause}.
	 * @param ctx the parse tree
	 */
	void enterLetClause(XQueryParser.LetClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#letClause}.
	 * @param ctx the parse tree
	 */
	void exitLetClause(XQueryParser.LetClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(XQueryParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(XQueryParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#returnClause}.
	 * @param ctx the parse tree
	 */
	void enterReturnClause(XQueryParser.ReturnClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#returnClause}.
	 * @param ctx the parse tree
	 */
	void exitReturnClause(XQueryParser.ReturnClauseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqCondOr}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterXqCondOr(XQueryParser.XqCondOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqCondOr}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitXqCondOr(XQueryParser.XqCondOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqCondEmpty}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterXqCondEmpty(XQueryParser.XqCondEmptyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqCondEmpty}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitXqCondEmpty(XQueryParser.XqCondEmptyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqCondSatisfy}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterXqCondSatisfy(XQueryParser.XqCondSatisfyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqCondSatisfy}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitXqCondSatisfy(XQueryParser.XqCondSatisfyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqCondNot}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterXqCondNot(XQueryParser.XqCondNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqCondNot}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitXqCondNot(XQueryParser.XqCondNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqCondEq}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterXqCondEq(XQueryParser.XqCondEqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqCondEq}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitXqCondEq(XQueryParser.XqCondEqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqCondXqParenthesis}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterXqCondXqParenthesis(XQueryParser.XqCondXqParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqCondXqParenthesis}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitXqCondXqParenthesis(XQueryParser.XqCondXqParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqCondIs}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterXqCondIs(XQueryParser.XqCondIsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqCondIs}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitXqCondIs(XQueryParser.XqCondIsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqCondAnd}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterXqCondAnd(XQueryParser.XqCondAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqCondAnd}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitXqCondAnd(XQueryParser.XqCondAndContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#joinClause}.
	 * @param ctx the parse tree
	 */
	void enterJoinClause(XQueryParser.JoinClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#joinClause}.
	 * @param ctx the parse tree
	 */
	void exitJoinClause(XQueryParser.JoinClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#attNames}.
	 * @param ctx the parse tree
	 */
	void enterAttNames(XQueryParser.AttNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#attNames}.
	 * @param ctx the parse tree
	 */
	void exitAttNames(XQueryParser.AttNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#var}.
	 * @param ctx the parse tree
	 */
	void enterVar(XQueryParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#var}.
	 * @param ctx the parse tree
	 */
	void exitVar(XQueryParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#tagName}.
	 * @param ctx the parse tree
	 */
	void enterTagName(XQueryParser.TagNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#tagName}.
	 * @param ctx the parse tree
	 */
	void exitTagName(XQueryParser.TagNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#attName}.
	 * @param ctx the parse tree
	 */
	void enterAttName(XQueryParser.AttNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#attName}.
	 * @param ctx the parse tree
	 */
	void exitAttName(XQueryParser.AttNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#fileName}.
	 * @param ctx the parse tree
	 */
	void enterFileName(XQueryParser.FileNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#fileName}.
	 * @param ctx the parse tree
	 */
	void exitFileName(XQueryParser.FileNameContext ctx);
}