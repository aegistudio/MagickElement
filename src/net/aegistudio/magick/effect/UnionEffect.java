package net.aegistudio.magick.effect;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.spell.SpellEffect;

public class UnionEffect implements SpellEffect {
	public Map<String, SpellEffect> subEffects = new TreeMap<String, SpellEffect>();
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		for(String entry : config.getKeys(false)) 
			if(entry.endsWith("Class")) {
				String subEffect = entry.substring(0, entry.length() - "Class".length());
				SpellEffect effectInstance = magick.loadInstance(SpellEffect.class, config, 
						entry, null, subEffect.concat("Config"), null);
				this.subEffects.put(subEffect, effectInstance);
			}
			else continue;
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		for(Entry<String, SpellEffect> subEffect : subEffects.entrySet()) {
			config.set(subEffect.getKey().concat("Class"), subEffect.getValue().getClass());
			String subEffectConfig = subEffect.getKey().concat("Config");
			ConfigurationSection subEffectSection = config.getConfigurationSection(subEffectConfig);
			if(!config.contains(subEffectConfig)) subEffectSection = config.createSection(subEffectConfig);
			subEffect.getValue().save(element, subEffectSection);
		}
	}

	@Override
	public void after(MagickElement element) {
		for(Entry<String, SpellEffect> subEffect : subEffects.entrySet()) 
			subEffect.getValue().after(element);
	}

	@Override
	public void spell(MagickElement element, Player sender, Location location, String[] params) {
		for(Entry<String, SpellEffect> subEffect : subEffects.entrySet()) 
			subEffect.getValue().spell(element, sender, location, params);
	}
}
