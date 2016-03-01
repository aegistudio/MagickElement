package net.aegistudio.magick.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Spawnable;

public class Flamepillar implements Spawnable {
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		
	}
	
	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		
	}

	@Override
	public void after(MagickElement element) {
		
	}

	public static final double PILLAR_FACTOR = 0.12;
	@SuppressWarnings("deprecation")
	@Override
	public void spawn(Location location) {
		location.getWorld().playSound(location, Sound.FIZZ, 1.0f, 1.0f);
		for(int i = 0; i < 3; i ++) {
			FallingBlock block = location.getWorld()
					.spawnFallingBlock(location, Material.FIRE, (byte) 0);
			block.setVelocity(new Vector(0, i * PILLAR_FACTOR, 0));
		}
	}
}
