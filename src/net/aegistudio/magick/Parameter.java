package net.aegistudio.magick;

/**
 * This is a wrapper for parameter. Arguments could be retrieved 
 * in specific type through this instance.
 * 
 * @author aegistudio
 */

public class Parameter {
	private final String[] parameter;
	public Parameter(String[] parameter) {
		this.parameter = parameter;
	}
	
	public int i(int index, int defaultValue) {
		try {
			return Integer.parseInt(parameter[index]);
		}
		catch(Throwable t) {
			return defaultValue;
		}
	}
	
	public double d(int index, double defaultValue) {
		try {
			return Double.parseDouble(parameter[index]);
		}
		catch(Throwable t) {
			return defaultValue;
		}
	}
	
	public String s(int index, String defaultValue) {
		try {
			return parameter[index];
		}
		catch(Throwable t) {
			return defaultValue;
		}
	}
	
	public int i(int index, int min, int max) {
		return Math.max(Math.min(this.i(index, min), max), min);
	}
	
	public double d(int index, double min, double max) {
		return Math.max(Math.min(this.d(index, min), max), min);		
	}
	
	public int len() {
		return parameter.length;
	}
	
	public int sel(int index, String... value) {
		try {
			for(int i = 0; i < value.length; i ++) {
				if(value[i].equalsIgnoreCase(parameter[index]))
					return i;
			}
			return 0;
		}
		catch(Throwable t) {
			return 0;
		}
	}
}
