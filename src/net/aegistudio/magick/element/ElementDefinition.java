package net.aegistudio.magick.element;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Module;

/**
 * Element is defined as magic ingredients that would turn
 * into magic power when brewed in cauldron.
 * 
 * @author aegistudio
 */

public class ElementDefinition implements Module {
	public final TreeMap<String, Integer> elementPoint = new TreeMap<String, Integer>();
	public int getElementValue(String elementName) {
		Integer elementValue = this.elementPoint.get(elementName.toLowerCase());
		if(elementValue == null) return 0;
		else return elementValue;
	}
	
	public void setElementPoint(String elementName, int point) {
		this.elementPoint.put(elementName.toLowerCase(), point);
	}
	
	/** Accumulate or reduce element factor from target **/
	public void accum(ElementDefinition target, int factor) {
		for(Entry<String, Integer> entry : target.elementPoint.entrySet()) 
			this.elementPoint.put(entry.getKey().toLowerCase(), entry.getValue() * factor 
					+ this.getElementValue(entry.getKey()));
	}
	
	/** Test whether all entries are smaller than or equal to zero **/
	public boolean redundant() {
		for(Entry<String, Integer> entry : elementPoint.entrySet()) 
			if(entry.getValue() > 0) return false;
		return true;
	}

	/** Test whether an element is helppful for reducing element price. **/
	public boolean helpful(ElementDefinition element) {
		for(Entry<String, Integer> entry : elementPoint.entrySet())
			if(entry.getValue().intValue() > 0)
				if(element.getElementValue(entry.getKey()) > 0) 
					return true;
		return false;
	}
	
	public void load(MagickElement element, ConfigurationSection section) {
		Set<String> keySet = section.getKeys(false);
		for(String key : keySet) 
			elementPoint.put(key.toLowerCase(), section.getInt(key));
	}
	
	public void save(MagickElement element, ConfigurationSection section) {
		for(Entry<String, Integer> entry : elementPoint.entrySet()) 
			section.set(entry.getKey().toLowerCase(), entry.getValue().intValue());
	}
}
