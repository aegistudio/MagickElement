package net.aegistudio.magick;

import org.bukkit.configuration.ConfigurationSection;

public interface Module {
	public void load(MagickElement magick, ConfigurationSection config) throws Exception;
	
	public void save(MagickElement element, ConfigurationSection config) throws Exception;
	
	public void after(MagickElement element);
}
