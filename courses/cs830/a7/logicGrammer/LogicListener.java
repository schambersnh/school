package logicGrammer;
// Generated from Logic.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LogicParser}.
 */
public interface LogicListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LogicParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(LogicParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogicParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(LogicParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogicParser#clause}.
	 * @param ctx the parse tree
	 */
	void enterClause(LogicParser.ClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogicParser#clause}.
	 * @param ctx the parse tree
	 */
	void exitClause(LogicParser.ClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogicParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(LogicParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogicParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(LogicParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogicParser#termList}.
	 * @param ctx the parse tree
	 */
	void enterTermList(LogicParser.TermListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogicParser#termList}.
	 * @param ctx the parse tree
	 */
	void exitTermList(LogicParser.TermListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogicParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(LogicParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogicParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(LogicParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogicParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(LogicParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogicParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(LogicParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogicParser#var}.
	 * @param ctx the parse tree
	 */
	void enterVar(LogicParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogicParser#var}.
	 * @param ctx the parse tree
	 */
	void exitVar(LogicParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogicParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(LogicParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogicParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(LogicParser.FunctionContext ctx);
}