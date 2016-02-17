package net.aegistudio.magick.spell;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;
import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Module;

public class SpellRegistry implements Module {
	public final TreeMap<String, SpellEntry> spellRegistries = new TreeMap<String, SpellEntry>();
	
	private final MagickElement element;
	public SpellRegistry(MagickElement magickElement) {
		this.element = magickElement;
	}
	
	public SpellEntry getSpell(String spellContent) {
		return spellRegistries.get(spellContent.split(" ")[0]);
	}
	
	public void load(MagickElement element, ConfigurationSection spellSection) throws Exception {
		for(String spellKey : spellSection.getKeys(false)) {
			ConfigurationSection spell = spellSection.getConfigurationSection(spellKey);
			SpellEntry spellEntry = new SpellEntry(this.element);
			spellEntry.load(element, spell);
			spellRegistries.put(spellKey, spellEntry);
		}
	}
	
	public void save(MagickElement element, ConfigurationSection spellSection) throws Exception {
		for(Entry<String, SpellEntry> spellRegistry : spellRegistries.entrySet()) {
			String spellKey = spellRegistry.getKey(); SpellEntry spellEntry = spellRegistry.getValue();
			if(!spellSection.contains(spellKey)) spellSection.createSection(spellKey);
			ConfigurationSection spell = spellSection.getConfigurationSection(spellKey);
			spellEntry.save(element, spell);
		}
	}
}
