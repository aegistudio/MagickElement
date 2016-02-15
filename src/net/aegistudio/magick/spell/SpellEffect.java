package net.aegistudio.magick.spell;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.aegistudio.magick.MagickElement;

public interface SpellEffect {
	public void spell(MagickElement element, Player sender, Location location, String[] params);
	
	public void load(ConfigurationSection spellConfig);
	
	public void save(ConfigurationSection spellConfig);	
}
