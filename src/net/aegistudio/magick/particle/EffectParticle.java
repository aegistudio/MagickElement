package net.aegistudio.magick.particle;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import net.aegistudio.magick.Configurable;
import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Spawnable;

public class EffectParticle implements Spawnable {
	public @Configurable(value = Configurable.Type.ENUM, name = "effectType") Effect effect;
	public @Configurable(value = Configurable.Type.CONSTANT, name = "effectData") int data; 
	
	public EffectParticle() {}
	
	public EffectParticle(Effect effect) {
		this.effect = effect;
		this.data = 0;
	}
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		magick.loadConfigurable(this, config);
	}

	@Override
	public void save(MagickElement magick, ConfigurationSection config) throws Exception {
		magick.saveConfigurable(this, config);
	}

	@Override
	public void after(MagickElement element) {	}

	@Override
	public void spawn(Location location, String[] arguments) {
		location.getWorld().playEffect(location, effect, data);
	}
}
