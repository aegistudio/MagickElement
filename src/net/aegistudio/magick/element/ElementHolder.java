package net.aegistudio.magick.element;

import java.util.Map.Entry;
import java.util.TreeMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Module;

public class ElementHolder implements Module {
	public final TreeMap<ItemDamagePair, ElementDefinition> element = new TreeMap<ItemDamagePair, ElementDefinition>();
	public final TreeMap<ItemDamagePair, ItemDamagePair> transform = new TreeMap<ItemDamagePair, ItemDamagePair>();
	
	public ElementDefinition definition(ItemStack itemStack) {
		ElementDefinition concreteMatch = element
				.get(new ItemDamagePair(itemStack.getType(), itemStack.getDurability()));
		if(concreteMatch != null) return concreteMatch;
		ElementDefinition wildcardMatch = element
				.get(new ItemDamagePair(itemStack.getType(), -1));
		if(wildcardMatch != null) return wildcardMatch;
		return new ElementDefinition();
	}
	
	public ItemDamagePair transform(ItemStack itemStack) {
		ItemDamagePair concreteMatch = transform
				.get(new ItemDamagePair(itemStack.getType(), itemStack.getDurability()));
		if(concreteMatch != null) return concreteMatch;
		ItemDamagePair wildcardMatch = transform
				.get(new ItemDamagePair(itemStack.getType(), -1));
		return wildcardMatch;
	}
	
	public static final String ELEMENT_ENTRY = "factor";
	public static final String TARGET_ENTRY = "target";
	
	public void load(MagickElement magick, ConfigurationSection parent) {
		for(String materialKey : parent.getKeys(false)) {
			ItemDamagePair source = ItemDamagePair.parse(materialKey);
			if(source == null) continue;
			ConfigurationSection section = parent.getConfigurationSection(materialKey);
			
			ElementDefinition itemElement = new ElementDefinition();
			if(section.contains(ELEMENT_ENTRY)) {
				ConfigurationSection element = section.getConfigurationSection(ELEMENT_ENTRY);
				itemElement.load(magick, element);
			}
			element.put(source, itemElement);
			
			if(!section.contains(TARGET_ENTRY)) continue;
			ItemDamagePair target = ItemDamagePair.parse(section.getString(TARGET_ENTRY));
			transform.put(source, target);
		}
	}
	
	public void save(MagickElement magick, ConfigurationSection parent) {
		for(Entry<ItemDamagePair, ElementDefinition> sourcePair : element.entrySet()) {
			String itemEntry = sourcePair.getKey().toString();
			ConfigurationSection section;
			if(parent.contains(itemEntry)) 
				section = parent.getConfigurationSection(itemEntry);
			else section = parent.createSection(itemEntry);
			
			ConfigurationSection itemElement;
			if(section.contains(ELEMENT_ENTRY))
				itemElement = section.getConfigurationSection(ELEMENT_ENTRY);
			else itemElement = section.createSection(ELEMENT_ENTRY);
			sourcePair.getValue().save(magick, itemElement);
			
			ItemDamagePair target = transform.get(sourcePair.getKey());
			if(target != null) section.set(TARGET_ENTRY, target.toString());
		}
	}
}
