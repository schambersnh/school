import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import logicGrammer.LogicBaseListener;
import logicGrammer.LogicLexer;
import logicGrammer.LogicParser;
import logicGrammer.LogicParser.ClauseContext;
import logicGrammer.LogicParser.ConstantContext;
import logicGrammer.LogicParser.FunctionContext;
import logicGrammer.LogicParser.PredicateContext;
import logicGrammer.LogicParser.VarContext;

public class GrammarListener extends LogicBaseListener {
	private Clause currentClause;
	private Predicate currentPredicate;
	private String prefix;
	private Stack<Function> funcStack;
	
	public GrammarListener(String prefix, Clause currentClause){
		super();
		this.currentClause = currentClause;
		this.prefix = prefix;
		currentClause = new Clause();
		funcStack = new Stack<Function>();
	}
		
	public void enterPredicate(PredicateContext ctx){
		currentPredicate = new Predicate(ctx.getText());
		currentClause.addPredicate(currentPredicate);
	}
	
	public void enterVar(VarContext ctx){
		Variable newVar = new Variable(prefix + ctx.getText());
		
		if(!funcStack.isEmpty()){
			funcStack.peek().addTerm(newVar);
		}
		else
			currentPredicate.addTerm(newVar);
	}

	public void enterConstant(ConstantContext ctx){
		Constant constant = new Constant(ctx.getText());
		if(!funcStack.isEmpty()){
			funcStack.peek().addTerm(constant);
		}
		else
			currentPredicate.addTerm(constant);
	}
	
	public void enterFunction(FunctionContext ctx){
		Function newFunc = new Function(ctx.getText());
		if(!funcStack.isEmpty()){
			funcStack.peek().addTerm(newFunc);
		}
		else
			currentPredicate.addTerm(newFunc);
		funcStack.push(newFunc);
	}
	
	public void exitFunction(FunctionContext ctx){
		funcStack.pop();
	}

	/**
	 * Return a filled in clause after parsing this sentence.
	 * @param prefix
	 * The prefix to append for variables in this clause
	 * @param sentence
	 * The sentence to parse
	 * @return
	 * A flled in clause after parsing the sentence
	 */
	public static Clause parseSentence(String prefix, String sentence) {
		LogicLexer lexer = new LogicLexer(new ANTLRInputStream(sentence));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LogicParser parser = new LogicParser(tokens);
		ClauseContext clauseContext = parser.clause();
		ParseTreeWalker walker = new ParseTreeWalker();
		
		
		Clause retval = new Clause();
		GrammarListener listener = new GrammarListener(prefix, retval);
		walker.walk(listener, clauseContext);
		return retval;
	}
}
