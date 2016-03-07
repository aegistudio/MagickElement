package net.aegistudio.magick.spell;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Module;

public interface SpellEffect extends Module {
	public void spell(MagickElement element, Entity sender, Location location, String[] params);
}
