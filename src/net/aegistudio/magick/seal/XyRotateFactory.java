package net.aegistudio.magick.seal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;

public class XyRotateFactory implements PainterFactory {
	private PainterFactory wrapped = new TrackedPainterFactory();	
	public static final String WRAPPED_CLASS = "wrappedClass";
	public static final String WRAPPED_CONFIG = "wrappedConfig";
	
	private double rotationSpeed = 0.10;
	public static final String ROTATION_SPEED = "rotationSpeed";
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		this.wrapped = magick.loadInstance(PainterFactory.class, config, WRAPPED_CLASS, 
				TrackedPainterFactory.class, WRAPPED_CONFIG, null);
		this.rotationSpeed = config.getDouble(ROTATION_SPEED);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		config.set(WRAPPED_CLASS, this.wrapped.getClass().getName());
		if(!config.contains(WRAPPED_CONFIG)) config.createSection(WRAPPED_CONFIG);
		this.wrapped.save(element, config.getConfigurationSection(WRAPPED_CONFIG));
		config.set(ROTATION_SPEED, rotationSpeed);
	}

	@Override
	public void after(MagickElement element) {
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
			}
		};
	}

}
