package net.aegistudio.magick.spell;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Module;

public interface SpellEffect extends Module {
	public void spell(MagickElement element, Player sender, Location location, String[] params);
}
