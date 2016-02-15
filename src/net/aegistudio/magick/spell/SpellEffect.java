package net.aegistudio.magick.spell;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public interface SpellEffect {
	public void spell(Player sender, Location location, String[] params);
	
	public void load(ConfigurationSection spellConfig);
	
	public void save(ConfigurationSection spellConfig);	
}
