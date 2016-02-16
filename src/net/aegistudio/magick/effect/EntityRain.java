package net.aegistudio.magick.effect;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.spell.SpellEffect;

public class EntityRain implements SpellEffect {
	public static final String TIER = "tier"; public int tier = 1;
	public static final String CLUSTER = "cluster"; public int cluster = 1;
	public static final String RANGE = "range";	public double range = 3.0;
	public static final String DELAY = "delay"; public int delay = 10;
	public static final String ENTITY_TYPE = "entity"; public EntityType entity = EntityType.ARROW;
	public static final String ON_FIRE = "onFire"; public boolean onFire = false;
	public static final String EXPLOSION_POWER = "explosionPower"; public double explosionPower = 3.0;
	public static final String HEIGHT = "height"; public int height = 128;
	
	@Override
	public void spell(MagickElement element, Player sender, Location location, String[] params) {
		for(int i = 0; i < tier; i ++)
			element.getServer().getScheduler().runTaskLater(element, 
					new EntityRainRunnable(this, sender.getLocation()), (long) (Math.random() * delay));
	}
	
	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) {
		if(spellConfig.contains(TIER)) tier = spellConfig.getInt(TIER);
		if(spellConfig.contains(CLUSTER)) cluster = spellConfig.getInt(CLUSTER);
		if(spellConfig.contains(RANGE)) range = spellConfig.getDouble(RANGE);
		if(spellConfig.contains(DELAY)) delay = spellConfig.getInt(DELAY);
		if(spellConfig.contains(ENTITY_TYPE)) entity = EntityType.valueOf(spellConfig.getString(ENTITY_TYPE));
		if(spellConfig.contains(ON_FIRE)) onFire = spellConfig.getBoolean(ON_FIRE);
		if(spellConfig.contains(EXPLOSION_POWER)) explosionPower = spellConfig.getDouble(EXPLOSION_POWER);
		if(spellConfig.contains(HEIGHT)) height = spellConfig.getInt(HEIGHT);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) {
		spellConfig.set(TIER, tier);
		spellConfig.set(CLUSTER, cluster);
		spellConfig.set(RANGE, range);
		spellConfig.set(DELAY, delay);
		spellConfig.set(ENTITY_TYPE, entity.toString());
		spellConfig.set(ON_FIRE, onFire);
		spellConfig.set(EXPLOSION_POWER, explosionPower);
		spellConfig.set(HEIGHT, height);
	}
}
