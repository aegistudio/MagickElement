package net.aegistudio.magick;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

/**
 * Expression provides an alternative way to enable more 
 * flexible calculation.
 * 
 * @author aegistudio
 */

public class Expression {
	protected static ScriptEngine engine;
	
	public final CompiledScript script;
	public final String expression;
	
	public static ScriptEngine getEngine() throws Exception {
		if(engine == null) {
			for(ScriptEngineFactory factory : new ScriptEngineManager().getEngineFactories())
				if(factory.getLanguageName().equalsIgnoreCase("ECMAScript")) {
					engine = factory.getScriptEngine();
					break;
				}
			if(engine == null)
				throw new Exception("No javascript engine!");
		}
		return engine;
	}
	
	public Expression(String expression) throws Exception {
		this.expression = expression;
		this.script = ((Compilable)getEngine()).compile(expression);
	}
	
	public <T> T getValue(String[] arguments, T defaultValue) {
		return getValue(new Parameter(arguments), defaultValue);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(Parameter params,  T defaultValue) {
		Object v = this.getValue(params);
		if(v == null) return defaultValue;
		else return (T) v;
	}
	
	public Object getValue(Parameter params) {
		try {
			Bindings bindings = getEngine().createBindings();
			bindings.put("param", params);
			this.bind(bindings);
			return script.eval(bindings);
		}
		catch(Throwable t) {
			return null;
		}
	}
	
	protected void bind(Bindings bindings) {	}
}
