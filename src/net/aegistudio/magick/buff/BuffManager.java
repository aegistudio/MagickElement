package net.aegistudio.magick.buff;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;

public interface BuffManager {
	public void buff(Entity entity, Buff buff, long duration);
	
	public void load(MagickElement element, ConfigurationSection section);
	
	public void save(MagickElement element, ConfigurationSection section);
}