package net.aegistudio.magick.book;

import org.bukkit.event.Listener;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Module;

public interface BookFactory extends Module {
	public MagickBook newMagickBook(MagickElement element);
	
	public Listener newBookListener(MagickElement element);
}
