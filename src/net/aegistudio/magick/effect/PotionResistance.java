package net.aegistudio.magick.effect;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.buff.Buff;
import net.aegistudio.magick.spell.SpellEffect;

/**
 * Make player immune to some debuffs.
 * @author aegistudio
 */

public class PotionResistance implements SpellEffect, Buff, Runnable, Listener {
	public static final String RESISTANCE_BUFFNAME = "buffName";
	public String buffName = "Resistance";
	
	public static final String POTIONEFFECT_TYPE = "potion";
	public PotionEffectType potionEffect;
	
	public static final String RESISTANCE_BEGIN = "beginEffect";
	public String beginEffect;
	
	public static final String RESISTANCE_END = "endEffect";
	public String endEffect;
	
	public static final String DURATION = "duration";
	public int duration = 50;
	
	private MagickElement element;
	
	public PotionResistance() {}
	
	public PotionResistance(PotionEffectType effect) {
		potionEffect = effect;
		beginEffect = "You're now immune to " + effect.getName().toLowerCase() + "!";
		endEffect = "You're vulnerable to " + effect.getName().toLowerCase() + " again...";
	}
	
	private final HashSet<LivingEntity> resistance = new HashSet<LivingEntity>();
	
	@Override
	public String name() {
		return buffName;
	}

	@Override
	public void buff(MagickElement element, Entity entity) {
		if(!(entity instanceof LivingEntity)) return;
		LivingEntity living = ((LivingEntity)entity);
		living.removePotionEffect(potionEffect);
		if(beginEffect != null) entity.sendMessage(beginEffect);
		this.resistance.add((LivingEntity) entity);
	}

	@Override
	public void remove(MagickElement element, Entity entity) {
		if(!(entity instanceof LivingEntity)) return;
		if(endEffect != null) entity.sendMessage(endEffect);
		this.resistance.remove(entity);
	}

	@Override
	public void spell(MagickElement element, Entity sender, Location location, String[] params) {
		element.buff.buff(sender, this, duration);
	}

	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) {
		this.beginEffect = spellConfig.getString(RESISTANCE_BEGIN);
		this.endEffect = spellConfig.getString(RESISTANCE_END);
		if(spellConfig.contains(DURATION))
			this.duration = spellConfig.getInt(DURATION);
		if(spellConfig.contains(RESISTANCE_BUFFNAME))
			this.buffName = spellConfig.getString(RESISTANCE_BUFFNAME);
		if(spellConfig.contains(POTIONEFFECT_TYPE))
			this.potionEffect = PotionEffectType.getByName(spellConfig.getString(POTIONEFFECT_TYPE));
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) {
		spellConfig.set(DURATION, duration);
		spellConfig.set(RESISTANCE_BEGIN, beginEffect);
		spellConfig.set(RESISTANCE_END, endEffect);
		spellConfig.set(RESISTANCE_BUFFNAME, buffName);
		spellConfig.set(POTIONEFFECT_TYPE, potionEffect.getName());
	}

	@EventHandler
	public void handlePotion(PotionSplashEvent event) {
		for(PotionEffect effect : event.getPotion().getEffects())
			if(effect.getType() == potionEffect)
				doProtect();
	}
	
	public void doProtect() {
		element.getServer().getScheduler().runTask(element, this);
	}
	
	@EventHandler
	public void handleDamage(EntityDamageEvent event) {
		if(this.resistance.contains(event.getEntity())) {
			if(event.getCause() == DamageCause.WITHER && this.potionEffect == PotionEffectType.WITHER) {
				event.setCancelled(true);
				doProtect();
			}
			if(event.getCause() == DamageCause.POISON && this.potionEffect == PotionEffectType.POISON) {
				event.setCancelled(true);
				doProtect();
			}
			if(event.getCause() == DamageCause.MAGIC && this.potionEffect == PotionEffectType.HARM) 
				event.setCancelled(true);
		}
	}
	
	@Override
	public void run() {
		for(LivingEntity entity : resistance) 
			entity.removePotionEffect(potionEffect);
	}

	@Override
	public void after(MagickElement element) {
		this.element = element;
		this.element.getServer().getPluginManager().registerEvents(this, element);
	}
}
