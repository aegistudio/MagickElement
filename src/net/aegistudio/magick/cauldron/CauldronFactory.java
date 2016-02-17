package net.aegistudio.magick.cauldron;

import org.bukkit.event.Listener;
import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Module;

public interface CauldronFactory extends Module {
	public Listener newCauldronListener(MagickElement element);
	
	public CauldronInventoryHandler newInventoryHanlder(MagickElement element);
}
