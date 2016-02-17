package net.aegistudio.magick.effect;

import java.util.TreeSet;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.buff.Buff;
import net.aegistudio.magick.spell.SpellEffect;

public class FeatherFall implements SpellEffect, Listener, Buff {
	private final TreeSet<Integer> protecting = new TreeSet<Integer>();
	
	@Override
	public void spell(MagickElement element, Player sender, Location location, String[] params) {
		element.buff.buff(sender, this, duration);
	}

	public static final String EFFECT_BEGIN = "effectBegin"; public String effectBegin = "You feel you were as light as a swallow...";
	public static final String EFFECT_END = "effectEnd"; public String effectEnd = "You could feel the pull of gravity again...";
	
	public static final String DURATION = "duration"; public long duration = 200;
	public static final String VELOCITY = "velocity"; public double velocity = 0.2;
	public static final String EFFECT_BUFFNAME = "buffName"; public String buffName = "Feather Fall";
	
	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) {
		element.getServer().getPluginManager().registerEvents(this, element);	
		
		if(spellConfig.contains(EFFECT_BEGIN)) effectBegin = spellConfig.getString(EFFECT_BEGIN);
		if(spellConfig.contains(EFFECT_END)) effectEnd = spellConfig.getString(EFFECT_END);
		if(spellConfig.contains(DURATION)) duration = spellConfig.getLong(DURATION);
		if(spellConfig.contains(VELOCITY)) velocity = spellConfig.getDouble(VELOCITY);
		if(spellConfig.contains(EFFECT_BUFFNAME)) buffName = spellConfig.getString(EFFECT_BUFFNAME);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) {
		element.getServer().getPluginManager().registerEvents(this, element);	
		
		spellConfig.set(EFFECT_BEGIN, effectBegin);
		spellConfig.set(EFFECT_END, effectEnd);
		spellConfig.set(DURATION, duration);
		spellConfig.set(VELOCITY, velocity);
		spellConfig.set(EFFECT_BUFFNAME, buffName);
	}

	@EventHandler
	public void handleFallSpeed(PlayerMoveEvent event) {
		if(!protecting.contains(event.getPlayer().getEntityId())) return;
		if(event.getPlayer().getVelocity().getY() < -velocity) {
			event.getPlayer().setVelocity(event.getPlayer().getVelocity().setY(-velocity));
			event.getPlayer().getLocation().getWorld()
				.playEffect(event.getPlayer().getLocation(),
						Effect.FLYING_GLYPH, null);
			event.getPlayer().setFallDistance(0);
		}
	}

	@Override
	public String name() {
		return buffName;
	}

	@Override
	public void buff(MagickElement element, Entity entity) {
		this.protecting.add(entity.getEntityId());
		entity.sendMessage(effectBegin);
	}

	@Override
	public void remove(MagickElement element, Entity entity) {
		this.protecting.remove(entity.getEntityId());
		entity.sendMessage(effectEnd);
	}
}
