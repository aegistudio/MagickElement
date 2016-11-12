package net.aegistudio.magick.seal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;

public class XyRotateFactory extends WrappedPainterFactory {
	private double rotationSpeed = 0.10;
	public static final String ROTATION_SPEED = "rotationSpeed";
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		super.load(magick, config);
		this.rotationSpeed = config.getDouble(ROTATION_SPEED);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		super.save(element, config);
		config.set(ROTATION_SPEED, rotationSpeed);
	}

	@Override
	public Painter newPainter(Entity entity) {
		double cos = Math.cos(Math.PI * rotationSpeed);
		double sin = Math.sin(Math.PI * rotationSpeed);
		double[][] rotation = new double[][] {
				{+cos, -sin, 0, 0},
				{+sin, +cos, 0, 0},
				{   0,    0, 1, 0},
				{   0,    0, 0, 1},
		};
		return new TransformPainter(this.wrapped.newPainter(entity), rotation) {
			@Override
			public void end() {
				this.multTrans();
				next.end();
			}
		};
	}
}
