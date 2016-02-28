package net.aegistudio.magick.seal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;

public class PitchOrientFactory extends WrappedPainterFactory {
	protected double initPhase = 0;
	public static final String INIT_PHASE = "initPhase";
	
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		super.load(magick, config);
		if(config.contains(INIT_PHASE)) 
			initPhase = config.getDouble(INIT_PHASE);
	}
	
	public void save(MagickElement magick, ConfigurationSection config) throws Exception {
		super.save(magick, config);
		config.set(INIT_PHASE, initPhase);
	}
	
	@Override
	public Painter newPainter(Entity entity) {
		return new PitchOrientPainter(this.wrapped.newPainter(entity), entity, initPhase);
	}
}
