package net.aegistudio.magick.seal;

import org.bukkit.entity.Entity;

import net.aegistudio.magick.Spawnable;

public class TrackedPainter implements Painter {
	private final Entity target;
	private final Spawnable effect;
	private final double scale;
	
	public TrackedPainter(Entity target, Spawnable effect, double scale) {
		this.target = target;
		this.effect = effect;
		this.scale = scale;
	}
	
	@Override
	public void paint(double x, double z, double y, String[] arguments) {
		this.effect.spawn(this.target.getLocation()
				.add(x * scale, y * scale, z * scale), arguments);
	}

	@Override
	public void end() {
		
	}
}
