package net.aegistudio.magick.seal;

import org.bukkit.entity.Entity;

import net.aegistudio.magick.particle.Particle;

public class TrackedPainter implements Painter {
	private final Entity target;
	private final Particle effect;
	private final double scale;
	public TrackedPainter(Entity target, Particle effect, double scale) {
		this.target = target;
		this.effect = effect;
		this.scale = scale;
	}
	
	@Override
	public void paint(double x, double z, double y) {
		this.effect.play(this.target.getLocation().add(x * scale, y * scale, z * scale));
	}
}
