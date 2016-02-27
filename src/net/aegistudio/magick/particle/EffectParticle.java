package net.aegistudio.magick.particle;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import net.aegistudio.magick.MagickElement;

public class EffectParticle implements Particle {
	Effect effect; public static final String EFFECT_TYPE = "effectType";
	int data; public static final String EFFECT_DATA = "effectData";
	
	public EffectParticle() {}
	
	public EffectParticle(Effect effect) {
		this.effect = effect;
		this.data = 0;
	}
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		if(config.contains(EFFECT_TYPE)) 
			effect = Effect.valueOf(config.getString(EFFECT_TYPE));
		if(config.contains(EFFECT_DATA))
			data = config.getInt(EFFECT_DATA);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		config.set(EFFECT_TYPE, effect.toString());
		config.set(EFFECT_DATA, data);
	}

	@Override
	public void after(MagickElement element) {	}

	@Override
	public void play(Location location) {
		location.getWorld().playEffect(location, effect, data);
	}
}
