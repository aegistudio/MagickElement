package net.aegistudio.magick;

public class AlgebraExpression {
	private Expression expression;
	private final Double value;
	private final String expressionString;
	
	public AlgebraExpression(String expression) throws Exception {
		this.expressionString = expression;
		Double v;
		try {
			v = Double.parseDouble(expression);
		}
		catch(Throwable t) {
			v = null;
			this.expression = new Expression(expression);
		}
		this.value = v;
	}
	
	public Object getValue(Parameter params, double defaultValue) {
		if(value != null) return value;
		return expression.getValue(params, defaultValue);
	}
	
	public double getDouble(Parameter params) {
		Object result = this.getValue(params, 0.0);
		if(result instanceof Double)
			return ((Double)result);
		else if(result instanceof Float) 
			return ((Float)result);
		else if(result instanceof Integer)
			return ((Integer)result);
		else if(result instanceof Long)
			return ((Long)result);
		else if(result instanceof Boolean)
			return ((Boolean)result)? 1.0:0.0;
		return 0.0;
	}
	
	public int getInt(Parameter params) {
		return (int)this.getDouble(params);
	}
	
	public boolean isConstant() {
		return this.value != null;
	}
	
	public boolean getBoolean(Parameter params) {
		return this.getDouble(params) >= 0.5;
	}
	
	public String getExpression() {
		return this.expressionString;
	}
	
	public String toString() {
		return this.getExpression();
	}
}
