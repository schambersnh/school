package logicGrammer;
// Generated from Logic.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LogicParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		CapitalizedName=1, LowercaseVariableName=2, OpenParen=3, ClosedParen=4, 
		Comma=5, Pipe=6, Not=7, Newline=8;
	public static final int
		RULE_prog = 0, RULE_clause = 1, RULE_predicate = 2, RULE_termList = 3, 
		RULE_term = 4, RULE_constant = 5, RULE_var = 6, RULE_function = 7;
	public static final String[] ruleNames = {
		"prog", "clause", "predicate", "termList", "term", "constant", "var", 
		"function"
	};

	private static final String[] _LITERAL_NAMES = {
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "CapitalizedName", "LowercaseVariableName", "OpenParen", "ClosedParen", 
		"Comma", "Pipe", "Not", "Newline"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Logic.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LogicParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public List<ClauseContext> clause() {
			return getRuleContexts(ClauseContext.class);
		}
		public ClauseContext clause(int i) {
			return getRuleContext(ClauseContext.class,i);
		}
		public List<TerminalNode> Newline() { return getTokens(LogicParser.Newline); }
		public TerminalNode Newline(int i) {
			return getToken(LogicParser.Newline, i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).exitProg(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(16);
				clause();
				setState(17);
				match(Newline);
				}
				}
				setState(21); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CapitalizedName || _la==Not );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClauseContext extends ParserRuleContext {
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public TerminalNode Pipe() { return getToken(LogicParser.Pipe, 0); }
		public ClauseContext clause() {
			return getRuleContext(ClauseContext.class,0);
		}
		public ClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).enterClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).exitClause(this);
		}
	}

	public final ClauseContext clause() throws RecognitionException {
		ClauseContext _localctx = new ClauseContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_clause);
		try {
			setState(28);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(23);
				predicate();
				setState(24);
				match(Pipe);
				setState(25);
				clause();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(27);
				predicate();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateContext extends ParserRuleContext {
		public TerminalNode CapitalizedName() { return getToken(LogicParser.CapitalizedName, 0); }
		public TerminalNode OpenParen() { return getToken(LogicParser.OpenParen, 0); }
		public TermListContext termList() {
			return getRuleContext(TermListContext.class,0);
		}
		public TerminalNode ClosedParen() { return getToken(LogicParser.ClosedParen, 0); }
		public TerminalNode Not() { return getToken(LogicParser.Not, 0); }
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).enterPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).exitPredicate(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_predicate);
		try {
			setState(41);
			switch (_input.LA(1)) {
			case CapitalizedName:
				enterOuterAlt(_localctx, 1);
				{
				setState(30);
				match(CapitalizedName);
				setState(31);
				match(OpenParen);
				setState(32);
				termList();
				setState(33);
				match(ClosedParen);
				}
				break;
			case Not:
				enterOuterAlt(_localctx, 2);
				{
				setState(35);
				match(Not);
				setState(36);
				match(CapitalizedName);
				setState(37);
				match(OpenParen);
				setState(38);
				termList();
				setState(39);
				match(ClosedParen);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermListContext extends ParserRuleContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TerminalNode Comma() { return getToken(LogicParser.Comma, 0); }
		public TermListContext termList() {
			return getRuleContext(TermListContext.class,0);
		}
		public TermListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).enterTermList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).exitTermList(this);
		}
	}

	public final TermListContext termList() throws RecognitionException {
		TermListContext _localctx = new TermListContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_termList);
		try {
			setState(48);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(43);
				term();
				setState(44);
				match(Comma);
				setState(45);
				termList();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(47);
				term();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public FunctionContext function() {
			return getRuleContext(FunctionContext.class,0);
		}
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_term);
		try {
			setState(53);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(50);
				constant();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				function();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(52);
				var();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public TerminalNode CapitalizedName() { return getToken(LogicParser.CapitalizedName, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).exitConstant(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_constant);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			match(CapitalizedName);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarContext extends ParserRuleContext {
		public TerminalNode LowercaseVariableName() { return getToken(LogicParser.LowercaseVariableName, 0); }
		public VarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).enterVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).exitVar(this);
		}
	}

	public final VarContext var() throws RecognitionException {
		VarContext _localctx = new VarContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_var);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			match(LowercaseVariableName);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionContext extends ParserRuleContext {
		public TerminalNode CapitalizedName() { return getToken(LogicParser.CapitalizedName, 0); }
		public TerminalNode OpenParen() { return getToken(LogicParser.OpenParen, 0); }
		public TermListContext termList() {
			return getRuleContext(TermListContext.class,0);
		}
		public TerminalNode ClosedParen() { return getToken(LogicParser.ClosedParen, 0); }
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LogicListener ) ((LogicListener)listener).exitFunction(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_function);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(CapitalizedName);
			setState(60);
			match(OpenParen);
			setState(61);
			termList();
			setState(62);
			match(ClosedParen);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\nC\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\6\2\26"+
		"\n\2\r\2\16\2\27\3\3\3\3\3\3\3\3\3\3\5\3\37\n\3\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\5\4,\n\4\3\5\3\5\3\5\3\5\3\5\5\5\63\n\5\3\6\3\6"+
		"\3\6\5\68\n\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\2\2\n\2\4\6\b\n"+
		"\f\16\20\2\2@\2\25\3\2\2\2\4\36\3\2\2\2\6+\3\2\2\2\b\62\3\2\2\2\n\67\3"+
		"\2\2\2\f9\3\2\2\2\16;\3\2\2\2\20=\3\2\2\2\22\23\5\4\3\2\23\24\7\n\2\2"+
		"\24\26\3\2\2\2\25\22\3\2\2\2\26\27\3\2\2\2\27\25\3\2\2\2\27\30\3\2\2\2"+
		"\30\3\3\2\2\2\31\32\5\6\4\2\32\33\7\b\2\2\33\34\5\4\3\2\34\37\3\2\2\2"+
		"\35\37\5\6\4\2\36\31\3\2\2\2\36\35\3\2\2\2\37\5\3\2\2\2 !\7\3\2\2!\"\7"+
		"\5\2\2\"#\5\b\5\2#$\7\6\2\2$,\3\2\2\2%&\7\t\2\2&\'\7\3\2\2\'(\7\5\2\2"+
		"()\5\b\5\2)*\7\6\2\2*,\3\2\2\2+ \3\2\2\2+%\3\2\2\2,\7\3\2\2\2-.\5\n\6"+
		"\2./\7\7\2\2/\60\5\b\5\2\60\63\3\2\2\2\61\63\5\n\6\2\62-\3\2\2\2\62\61"+
		"\3\2\2\2\63\t\3\2\2\2\648\5\f\7\2\658\5\20\t\2\668\5\16\b\2\67\64\3\2"+
		"\2\2\67\65\3\2\2\2\67\66\3\2\2\28\13\3\2\2\29:\7\3\2\2:\r\3\2\2\2;<\7"+
		"\4\2\2<\17\3\2\2\2=>\7\3\2\2>?\7\5\2\2?@\5\b\5\2@A\7\6\2\2A\21\3\2\2\2"+
		"\7\27\36+\62\67";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}