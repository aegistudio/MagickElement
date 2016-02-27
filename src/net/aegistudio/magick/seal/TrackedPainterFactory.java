package net.aegistudio.magick.seal;

import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.particle.EffectParticle;
import net.aegistudio.magick.particle.Particle;

public class TrackedPainterFactory implements PainterFactory {
	private double scale = 5.0; public static final String SCALE = "scale";
	
	public Particle effect = new EffectParticle(Effect.HAPPY_VILLAGER);
	public static final String EFFECT_CLASS = "effectClass";
	public static final String EFFECT_CONFIG = "effectConfig";
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		if(config.contains(SCALE)) scale = config.getDouble(SCALE);
		this.effect = magick.loadInstance(Particle.class, config, EFFECT_CLASS, null, EFFECT_CONFIG, null);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		config.set(SCALE, scale);
		config.set(EFFECT_CLASS, effect.getClass().getName());
		
		if(!config.contains(EFFECT_CONFIG)) config.createSection(EFFECT_CONFIG);
		this.effect.save(element, config.getConfigurationSection(EFFECT_CONFIG));
	}

	@Override
	public void after(MagickElement element) {	}

	@Override
	public Painter newPainter(Entity entity) {
		return new TrackedPainter(entity, effect, scale);
	}
}
