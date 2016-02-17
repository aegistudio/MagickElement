package net.aegistudio.magick.buff;

import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;

public interface Buff {
	/** called when required to display **/
	public String name();
	
	/** called when buff begins. **/
	public void buff(MagickElement element, Entity entity);
	
	/** called when buff ends. **/
	public void remove(MagickElement element, Entity entity);
}
