// Generated from XPath.g4 by ANTLR 4.9.1

	package antlr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link XPathParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface XPathVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code ApRoot}
	 * labeled alternative in {@link XPathParser#ap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApRoot(XPathParser.ApRootContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ApCurrent}
	 * labeled alternative in {@link XPathParser#ap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApCurrent(XPathParser.ApCurrentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpParent}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpParent(XPathParser.RpParentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpAttName}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpAttName(XPathParser.RpAttNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpFromCurr}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpFromCurr(XPathParser.RpFromCurrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpText}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpText(XPathParser.RpTextContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpTagName}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpTagName(XPathParser.RpTagNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpChildren}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpChildren(XPathParser.RpChildrenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpCurrent}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpCurrent(XPathParser.RpCurrentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpConcat}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpConcat(XPathParser.RpConcatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpRoot}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpRoot(XPathParser.RpRootContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpFilter}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpFilter(XPathParser.RpFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RpPathNodes}
	 * labeled alternative in {@link XPathParser#rp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRpPathNodes(XPathParser.RpPathNodesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FilterEqConst}
	 * labeled alternative in {@link XPathParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterEqConst(XPathParser.FilterEqConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FilterNot}
	 * labeled alternative in {@link XPathParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterNot(XPathParser.FilterNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FilterOr}
	 * labeled alternative in {@link XPathParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterOr(XPathParser.FilterOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FilterAnd}
	 * labeled alternative in {@link XPathParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterAnd(XPathParser.FilterAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FilterRp}
	 * labeled alternative in {@link XPathParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterRp(XPathParser.FilterRpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FilterEq}
	 * labeled alternative in {@link XPathParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterEq(XPathParser.FilterEqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FilterIs}
	 * labeled alternative in {@link XPathParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterIs(XPathParser.FilterIsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FilterCurrent}
	 * labeled alternative in {@link XPathParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterCurrent(XPathParser.FilterCurrentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#tagName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagName(XPathParser.TagNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#attName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttName(XPathParser.AttNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#fileName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFileName(XPathParser.FileNameContext ctx);
}