package net.aegistudio.magick.cauldron;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import net.aegistudio.magick.MagickElement;

public interface CauldronFactory {
	public void loadConfig(ConfigurationSection cauldronConfig);
	
	public Listener newCauldronListener(MagickElement element);
	
	public CauldronInventoryHandler newInventoryHanlder(MagickElement element);
}
