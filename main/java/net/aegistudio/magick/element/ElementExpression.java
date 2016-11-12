package net.aegistudio.magick.element;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

import net.aegistudio.magick.AlgebraExpression;
import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Module;
import net.aegistudio.magick.Parameter;

public class ElementExpression implements Module {
	public final TreeMap<String, AlgebraExpression> defExpressions
			= new TreeMap<String, AlgebraExpression>();
	
	public void setExpression(String element, String expression) throws Exception {
		this.defExpressions.put(element, new AlgebraExpression(expression));
	}
	
	public ElementDefinition get(String[] arguments) {
		Parameter param = new Parameter(arguments);
		ElementDefinition definition = new ElementDefinition();
		for(Entry<String, AlgebraExpression> expression : defExpressions.entrySet()) 
			definition.setElementPoint(expression.getKey(), expression.getValue().getInt(param));
		return definition;
	}
	
	@Override
	public void load(MagickElement magick, ConfigurationSection section) throws Exception {
		Set<String> keySet = section.getKeys(false);
		for(String key : keySet) 
			defExpressions.put(key.toLowerCase(), new AlgebraExpression(section.getString(key)));
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		for(Entry<String, AlgebraExpression> entry : defExpressions.entrySet()) 
			config.set(entry.getKey().toLowerCase(), entry.getValue().getExpression());
	}

	@Override
	public void after(MagickElement element) {
		
	}
}
