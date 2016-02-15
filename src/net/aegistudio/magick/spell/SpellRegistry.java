package net.aegistudio.magick.spell;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;
import net.aegistudio.magick.MagickElement;

public class SpellRegistry {
	public final TreeMap<String, SpellEntry> spellRegistries = new TreeMap<String, SpellEntry>();
	
	private final MagickElement element;
	public SpellRegistry(MagickElement magickElement) {
		this.element = magickElement;
	}
	
	public SpellEntry getSpell(String spellContent) {
		return spellRegistries.get(spellContent.split(" ")[0]);
	}
	
	public void loadConfig(ConfigurationSection spellSection) throws Exception {
		for(String spellKey : spellSection.getKeys(false)) {
			ConfigurationSection spell = spellSection.getConfigurationSection(spellKey);
			SpellEntry spellEntry = new SpellEntry(this.element);
			spellEntry.load(spell);
			spellRegistries.put(spellKey, spellEntry);
		}
	}
	
	public void saveConfig(ConfigurationSection spellSection) throws Exception {
		for(Entry<String, SpellEntry> spellRegistry : spellRegistries.entrySet()) {
			String spellKey = spellRegistry.getKey(); SpellEntry spellEntry = spellRegistry.getValue();
			if(!spellSection.contains(spellKey)) spellSection.createSection(spellKey);
			ConfigurationSection spell = spellSection.getConfigurationSection(spellKey);
			spellEntry.save(spell);
		}
	}
}
