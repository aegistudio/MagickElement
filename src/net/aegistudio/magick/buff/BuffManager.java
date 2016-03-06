package net.aegistudio.magick.buff;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Module;

public interface BuffManager extends Module {
	public void buff(Entity entity, Buff buff, long duration);
	
	public void unbuff(Entity entity, Buff buff);
	
	public void load(MagickElement element, ConfigurationSection section);
	
	public void save(MagickElement element, ConfigurationSection section);
}