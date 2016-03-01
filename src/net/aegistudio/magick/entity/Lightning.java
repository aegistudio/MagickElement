package net.aegistudio.magick.entity;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Spawnable;

public class Lightning implements Spawnable {

	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {		}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {		}

	@Override
	public void after(MagickElement element) {		}

	@Override
	public void spawn(Location location) {
		location.getWorld().strikeLightning(location);
	}
}
