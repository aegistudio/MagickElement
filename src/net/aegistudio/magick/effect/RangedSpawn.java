package net.aegistudio.magick.effect;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Spawnable;
import net.aegistudio.magick.spell.SpellEffect;

public class RangedSpawn implements SpellEffect {
	public static final String TIER = "tier"; public int tier = 1;
	public static final String CLUSTER = "cluster"; public int cluster = 1;
	public static final String RANGE = "maxRange";	public double range = 5.0;
	public static final String MIN_RANGE = "minRange"; public double minRange = 1.0;
	public static final String LAG = "lag"; public long lag = 0;
	public static final String DELAY = "delay"; public long delay = 40;
	public static final String ENTITY_CLASS = "entityClass"; 
	public static final String ENTITY_CONFIG = "entityConfig";
	public Spawnable entity;
	
	@Override
	public void spell(MagickElement element, Player sender, Location location, String[] params) {
		new RangedSpawnRunnable(element, this, sender.getLocation()).start();
	}
	
	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) throws Exception {
		if(spellConfig.contains(TIER)) tier = spellConfig.getInt(TIER);
		if(spellConfig.contains(RANGE)) range = spellConfig.getDouble(RANGE);
		if(spellConfig.contains(MIN_RANGE)) minRange = spellConfig.getDouble(MIN_RANGE);
		if(spellConfig.contains(DELAY)) delay = spellConfig.getLong(DELAY);
		if(spellConfig.contains(LAG)) lag = spellConfig.getLong(LAG);
		if(spellConfig.contains(CLUSTER)) cluster = spellConfig.getInt(CLUSTER);
		this.entity = element.loadInstance(Spawnable.class, spellConfig, ENTITY_CLASS, null, ENTITY_CONFIG, null);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) throws Exception {
		spellConfig.set(TIER, tier);
		spellConfig.set(RANGE, range);
		spellConfig.set(MIN_RANGE, minRange);
		spellConfig.set(LAG, lag);
		spellConfig.set(DELAY, delay);
		spellConfig.set(CLUSTER, cluster);
		
		element.saveInstance(entity, spellConfig, ENTITY_CLASS, ENTITY_CONFIG);
	}

	@Override
	public void after(MagickElement element) {
		entity.after(element);
	}
}
