package net.aegistudio.magick.effect;

import java.util.TreeSet;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.spell.SpellEffect;

public class FeatherFall implements SpellEffect, Listener {
	private final TreeSet<String> protecting = new TreeSet<String>();
	
	@Override
	public void spell(MagickElement element, Player sender, Location location, String[] params) {
		if(!protecting.contains(sender.getName())) {
			protecting.add(sender.getName());
			sender.sendMessage(effectBegin);
			element.getServer().getScheduler().runTaskLater(element, new Runnable() {
				@Override
				public void run() {
					if(protecting.contains(sender.getName())) {
						protecting.remove(sender.getName());
						sender.sendMessage(effectEnd);
					}
				}
			}, duration);
		}
	}

	public static final String EFFECT_BEGIN = "effectBegin"; String effectBegin = "You feel you were as light as a swallow...";
	public static final String EFFECT_END = "effectEnd"; String effectEnd = "You could feel the pull of gravity again...";
	
	public static final String DURATION = "duration"; long duration = 2000;
	public static final String VELOCITY = "velocity"; double velocity = 0.2;
	
	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) {
		element.getServer().getPluginManager().registerEvents(this, element);	
		
		if(spellConfig.contains(EFFECT_BEGIN)) effectBegin = spellConfig.getString(EFFECT_BEGIN);
		if(spellConfig.contains(EFFECT_END)) effectEnd = spellConfig.getString(EFFECT_END);
		if(spellConfig.contains(DURATION)) duration = spellConfig.getLong(DURATION);
		if(spellConfig.contains(VELOCITY)) velocity = spellConfig.getDouble(VELOCITY);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) {
		spellConfig.set(EFFECT_BEGIN, effectBegin);
		spellConfig.set(EFFECT_END, effectEnd);
		spellConfig.set(DURATION, duration);
		spellConfig.set(VELOCITY, velocity);
	}
	
	@EventHandler
	public void handleFallSpeed(PlayerMoveEvent event) {
		if(!protecting.contains(event.getPlayer().getName())) return;
		if(event.getPlayer().getVelocity().getY() < -velocity)
			event.getPlayer().setVelocity(event.getPlayer().getVelocity().setY(-velocity));
	}
	
	@EventHandler
	public void handleFallDamage(EntityDamageEvent event) {
		if(event.getEntityType() != EntityType.PLAYER) return;
		if(event.getCause() != DamageCause.FALL) return;
		if(protecting.remove(((Player)event.getEntity()).getName())) {
			event.getEntity().sendMessage(effectEnd);
			event.setCancelled(true);
		}
	}
}
