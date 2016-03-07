package net.aegistudio.magick.particle;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Spawnable;

public class FireworkParticle implements Spawnable{
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		
	}

	@Override
	public void after(MagickElement element) {
		
	}

	@Override
	public void spawn(Location location) {
		Firework firework = location.getWorld().spawn(location, Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.addEffect(FireworkEffect.builder().withColor(Color.RED)
				.with(FireworkEffect.Type.BURST).build());
		meta.setPower(0);
		firework.setFireworkMeta(meta);
		firework.detonate();
	}
}
