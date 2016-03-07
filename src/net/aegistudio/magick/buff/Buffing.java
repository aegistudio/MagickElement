package net.aegistudio.magick.buff;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.spell.SpellEffect;

/**
 * Adding buffs to nearby entities. Can be used to filter player, hostiles
 * or other entities.
 * 
 * @author aegistudio
 */

public class Buffing implements SpellEffect {
	@Override
	public void spell(MagickElement element, Entity sender, Location location, String[] params) {
		
	}

	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) {
		
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) {
		
	}

	@Override
	public void after(MagickElement element) {
		
	}
}
