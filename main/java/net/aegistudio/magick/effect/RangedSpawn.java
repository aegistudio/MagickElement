package net.aegistudio.magick.effect;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.Configurable;
import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Spawnable;
import net.aegistudio.magick.spell.SpellEffect;

public class RangedSpawn implements SpellEffect {
	public @Configurable(Configurable.Type.CONSTANT) int tier = 1;
	public @Configurable(Configurable.Type.CONSTANT) int cluster = 1;
	public @Configurable(value = Configurable.Type.CONSTANT, name = "maxRange") double range = 5.0;
	public @Configurable(value = Configurable.Type.CONSTANT, name = "minRange") double minRange = 1.0;
	public @Configurable(Configurable.Type.CONSTANT) long lag = 0;
	public @Configurable(Configurable.Type.CONSTANT) long delay = 40;
	
	public static final String ENTITY_CLASS = "entityClass"; 
	public static final String ENTITY_CONFIG = "entityConfig";
	public Spawnable entity;
	
	@Override
	public void spell(MagickElement element, Entity sender, Location location, String[] params) {
		new RangedSpawnRunnable(element, this, sender.getLocation(), params).start();
	}
	
	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) throws Exception {
		element.loadConfigurable(this, spellConfig);
		this.entity = element.loadInstance(Spawnable.class, spellConfig, ENTITY_CLASS, null, ENTITY_CONFIG, null);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) throws Exception {
		element.saveConfigurable(this, spellConfig);
		element.saveInstance(entity, spellConfig, ENTITY_CLASS, ENTITY_CONFIG);
	}

	@Override
	public void after(MagickElement element) {
		entity.after(element);
	}
}
