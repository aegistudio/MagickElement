package net.aegistudio.magick.seal;

import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Spawnable;
import net.aegistudio.magick.particle.EffectParticle;

public class TrackedFactory implements PainterFactory {
	private double scale = 5.0; public static final String SCALE = "scale";
	
	public Spawnable effect = new EffectParticle(Effect.valueOf("HAPPY_VILLAGER"));
	public static final String EFFECT_CLASS = "effectClass";
	public static final String EFFECT_CONFIG = "effectConfig";
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		if(config.contains(SCALE)) scale = config.getDouble(SCALE);
		this.effect = magick.loadInstance(Spawnable.class, config, EFFECT_CLASS, null, EFFECT_CONFIG, null);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		config.set(SCALE, scale);
		
		element.saveInstance(this.effect, config, EFFECT_CLASS, EFFECT_CONFIG);
	}

	@Override
	public void after(MagickElement element) {	}

	@Override
	public Painter newPainter(Entity entity) {
		return new TrackedPainter(entity, effect, scale);
	}
}
