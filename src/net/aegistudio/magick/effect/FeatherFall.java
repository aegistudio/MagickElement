package net.aegistudio.magick.effect;

import java.util.TreeMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.spell.SpellEffect;

public class FeatherFall implements SpellEffect, Listener {
	private final TreeMap<String, Integer> protecting = new TreeMap<String, Integer>();
	
	public void increment(Player player, int count) {
		Integer countValue = protecting.get(player.getName());
		if(countValue != null) {
			countValue += count;
			if(countValue == 0) protecting.remove(player.getName());
			else protecting.put(player.getName(), countValue);
		}
		else protecting.put(player.getName(), count);
	}
	
	@Override
	public void spell(MagickElement element, Player sender, Location location, String[] params) {
		increment(sender, 1);
		sender.sendMessage(effectBegin);
		element.getServer().getScheduler().runTaskLater(element, new Runnable() {
			@Override
			public void run() {
				increment(sender, -1);
				if(!protecting.containsKey(sender.getName())) 
					sender.sendMessage(effectEnd);
			}
		}, duration);
	}

	public static final String EFFECT_BEGIN = "effectBegin"; public String effectBegin = "You feel you were as light as a swallow...";
	public static final String EFFECT_END = "effectEnd"; public String effectEnd = "You could feel the pull of gravity again...";
	
	public static final String DURATION = "duration"; public long duration = 200;
	public static final String VELOCITY = "velocity"; public double velocity = 0.2;
	
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
		if(!protecting.containsKey(event.getPlayer().getName())) return;
		if(event.getPlayer().getVelocity().getY() < -velocity) {
			event.getPlayer().setVelocity(event.getPlayer().getVelocity().setY(-velocity));
			event.getPlayer().getLocation().getWorld()
				.playEffect(event.getPlayer().getLocation(),
						Effect.FLYING_GLYPH, null);
			event.getPlayer().setFallDistance(0);
		}
	}
}
