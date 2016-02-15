package net.aegistudio.magick.book;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import net.aegistudio.magick.MagickElement;

public interface BookFactory {
	public void setConfig(ConfigurationSection config);
	
	public MagickBook newMagickBook(MagickElement element);
	
	public Listener newBookListener(MagickElement element);
}
