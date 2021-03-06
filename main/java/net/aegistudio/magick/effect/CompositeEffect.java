package net.aegistudio.magick.effect;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.AlgebraExpression;
import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.spell.SpellEffect;

public abstract class CompositeEffect implements SpellEffect {
	protected Map<String, CompositeEffectEntry> subEffects = new TreeMap<String, CompositeEffectEntry>();
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		for(String entry : config.getKeys(false)) 
			if(entry.endsWith("Class")) {
				String subEffect = entry.substring(0, entry.length() - "Class".length());
				SpellEffect effectInstance = magick.loadInstance(SpellEffect.class, config, 
						entry, null, subEffect.concat("Config"), null);
				CompositeEffectEntry compositeEntry = new CompositeEffectEntry();
				compositeEntry.effect = effectInstance;
				if(config.contains(subEffect.concat("Probability")))
					compositeEntry.probability = new AlgebraExpression(config.getString(subEffect.concat("Probability")));
				
				this.subEffects.put(subEffect, compositeEntry);
			}
			else continue;
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		for(Entry<String, CompositeEffectEntry> subEffect : subEffects.entrySet()) {
			element.saveInstance(subEffect.getValue().effect, config, 
					subEffect.getKey().concat("Class"), subEffect.getKey().concat("Config"));
			
			if(subEffect.getValue().probability.isConstant()) {
				if(subEffect.getValue().probability.getDouble(null) < 1.0)
					config.set(subEffect.getKey().concat("Probability"), 
							subEffect.getValue().probability.getExpression());
			}
			else config.set(subEffect.getKey().concat("Probability"), 
					subEffect.getValue().probability.getExpression());
		}
	}

	@Override
	public void after(MagickElement element) {
		for(Entry<String, CompositeEffectEntry> subEffect : subEffects.entrySet()) 
			subEffect.getValue().effect.after(element);
	}

	@Override
	public abstract void spell(MagickElement element, Entity sender, Location location, String[] params);
}
