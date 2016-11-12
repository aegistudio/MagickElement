package net.aegistudio.magick;

import org.bukkit.Location;

public interface Spawnable extends Module {
	public void spawn(Location location, String[] arguments);
}
