package logicGrammer;
// Generated from Logic.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LogicLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		CapitalizedName=1, LowercaseVariableName=2, OpenParen=3, ClosedParen=4, 
		Comma=5, Pipe=6, Not=7, Newline=8;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"CapitalizedName", "LowercaseVariableName", "OpenParen", "ClosedParen", 
		"Comma", "Pipe", "Not", "Newline"
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


	public LogicLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Logic.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\no\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\6\2\26"+
		"\n\2\r\2\16\2\27\3\2\6\2\33\n\2\r\2\16\2\34\3\2\6\2 \n\2\r\2\16\2!\3\2"+
		"\6\2%\n\2\r\2\16\2&\7\2)\n\2\f\2\16\2,\13\2\3\3\3\3\6\3\60\n\3\r\3\16"+
		"\3\61\3\3\6\3\65\n\3\r\3\16\3\66\3\3\6\3:\n\3\r\3\16\3;\3\3\6\3?\n\3\r"+
		"\3\16\3@\7\3C\n\3\f\3\16\3F\13\3\3\4\5\4I\n\4\3\4\3\4\5\4M\n\4\3\5\5\5"+
		"P\n\5\3\5\3\5\5\5T\n\5\3\6\5\6W\n\6\3\6\3\6\5\6[\n\6\3\7\5\7^\n\7\3\7"+
		"\3\7\5\7b\n\7\3\b\5\be\n\b\3\b\3\b\5\bi\n\b\3\t\6\tl\n\t\r\t\16\tm\2\2"+
		"\n\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\3\2\3\4\2\f\f\17\17\u0089\2\3\3"+
		"\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2"+
		"\17\3\2\2\2\2\21\3\2\2\2\3\23\3\2\2\2\5-\3\2\2\2\7H\3\2\2\2\tO\3\2\2\2"+
		"\13V\3\2\2\2\r]\3\2\2\2\17d\3\2\2\2\21k\3\2\2\2\23*\4C\\\2\24\26\4C\\"+
		"\2\25\24\3\2\2\2\26\27\3\2\2\2\27\25\3\2\2\2\27\30\3\2\2\2\30)\3\2\2\2"+
		"\31\33\4c|\2\32\31\3\2\2\2\33\34\3\2\2\2\34\32\3\2\2\2\34\35\3\2\2\2\35"+
		")\3\2\2\2\36 \4\62;\2\37\36\3\2\2\2 !\3\2\2\2!\37\3\2\2\2!\"\3\2\2\2\""+
		")\3\2\2\2#%\7a\2\2$#\3\2\2\2%&\3\2\2\2&$\3\2\2\2&\'\3\2\2\2\')\3\2\2\2"+
		"(\25\3\2\2\2(\32\3\2\2\2(\37\3\2\2\2($\3\2\2\2),\3\2\2\2*(\3\2\2\2*+\3"+
		"\2\2\2+\4\3\2\2\2,*\3\2\2\2-D\4c|\2.\60\4C\\\2/.\3\2\2\2\60\61\3\2\2\2"+
		"\61/\3\2\2\2\61\62\3\2\2\2\62C\3\2\2\2\63\65\4c|\2\64\63\3\2\2\2\65\66"+
		"\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\67C\3\2\2\28:\4\62;\298\3\2\2\2:"+
		";\3\2\2\2;9\3\2\2\2;<\3\2\2\2<C\3\2\2\2=?\7a\2\2>=\3\2\2\2?@\3\2\2\2@"+
		">\3\2\2\2@A\3\2\2\2AC\3\2\2\2B/\3\2\2\2B\64\3\2\2\2B9\3\2\2\2B>\3\2\2"+
		"\2CF\3\2\2\2DB\3\2\2\2DE\3\2\2\2E\6\3\2\2\2FD\3\2\2\2GI\7\"\2\2HG\3\2"+
		"\2\2HI\3\2\2\2IJ\3\2\2\2JL\7*\2\2KM\7\"\2\2LK\3\2\2\2LM\3\2\2\2M\b\3\2"+
		"\2\2NP\7\"\2\2ON\3\2\2\2OP\3\2\2\2PQ\3\2\2\2QS\7+\2\2RT\7\"\2\2SR\3\2"+
		"\2\2ST\3\2\2\2T\n\3\2\2\2UW\7\"\2\2VU\3\2\2\2VW\3\2\2\2WX\3\2\2\2XZ\7"+
		".\2\2Y[\7\"\2\2ZY\3\2\2\2Z[\3\2\2\2[\f\3\2\2\2\\^\7\"\2\2]\\\3\2\2\2]"+
		"^\3\2\2\2^_\3\2\2\2_a\7~\2\2`b\7\"\2\2a`\3\2\2\2ab\3\2\2\2b\16\3\2\2\2"+
		"ce\7\"\2\2dc\3\2\2\2de\3\2\2\2ef\3\2\2\2fh\7/\2\2gi\7\"\2\2hg\3\2\2\2"+
		"hi\3\2\2\2i\20\3\2\2\2jl\t\2\2\2kj\3\2\2\2lm\3\2\2\2mk\3\2\2\2mn\3\2\2"+
		"\2n\22\3\2\2\2\32\2\27\34!&(*\61\66;@BDHLOSVZ]adhm\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}