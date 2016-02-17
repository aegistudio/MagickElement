package net.aegistudio.magick.buff;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;

/**
 * Could be used to filter entities.
 * A source entity should be provided.
 * 
 * @author aegistudio
 */

public interface EntityFilter {
	public List<Entity> filter(Entity source);
	
	public void loadConfig(MagickElement element, ConfigurationSection configuration);
	
	public void saveConfig(MagickElement element, ConfigurationSection configuration);
}
