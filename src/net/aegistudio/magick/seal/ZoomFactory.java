package net.aegistudio.magick.seal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;

public class ZoomFactory extends WrappedPainterFactory {
	private double init = 0.0;
	public static final String INIT = "init";
	private double zoom = 0.1;
	public static final String ZOOM = "zoom";
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		super.load(magick, config);
		this.init = config.getDouble(INIT);
		this.zoom = config.getDouble(ZOOM);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		super.save(element, config);
		config.set(INIT, init);
		config.set(ZOOM, zoom);
	}

	@Override
	public Painter newPainter(Entity entity) {
		return new TransformPainter(this.wrapped.newPainter(entity), new double[][] {
			{zoom, 0, 0, 0},
			{0,	zoom, 0, 0},
			{0,	0, zoom, 0},
			{0, 0, 0, 0}
		}, new double[][] {
			{init, 0, 0, 0},
			{0,	init, 0, 0},
			{0,	0, init, 0},
			{0, 0, 0, 1}
		}){
			@Override
			public void end() {
				this.addTrans();
				this.next.end();
			}
		};
	}
}
