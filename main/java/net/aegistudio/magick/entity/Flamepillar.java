package net.aegistudio.magick.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.FallingBlock;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Spawnable;
import net.aegistudio.magick.compat.CompatibleSound;

public class Flamepillar implements Spawnable {
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		
	}
	
	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		
	}

	private MagickElement element;
	@Override
	public void after(MagickElement element) {
		this.element = element;
	}

	public static final double PILLAR_FACTOR = 0.12;
	@SuppressWarnings("deprecation")
	@Override
	public void spawn(Location location, String[] arguments) {
		location.getWorld().playSound(location, CompatibleSound.FIZZ.get(element), 1.0f, 1.0f);
		FallingBlock previous = null;
		for(int i = 0; i < 3; i ++) {
			FallingBlock block = location.getWorld()
					.spawnFallingBlock(location, Material.FIRE, (byte) 0);
			//block.setFireTicks(100);
			//block.setVelocity(new Vector(0, i * PILLAR_FACTOR, 0));
			if(previous != null) previous.setPassenger(block);
			previous = block;
		}
	}
}
