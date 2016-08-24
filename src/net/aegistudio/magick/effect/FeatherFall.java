package net.aegistudio.magick.effect;

import java.util.TreeSet;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.aegistudio.magick.Configurable;
import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.buff.Buff;
import net.aegistudio.magick.compat.CompatibleSound;
import net.aegistudio.magick.particle.MagickParticle;
import net.aegistudio.magick.spell.SpellEffect;

public class FeatherFall implements SpellEffect, Listener, Buff {
	private final TreeSet<Integer> protecting = new TreeSet<Integer>();
	
	@Override
	public void spell(MagickElement element, Entity sender, Location location, String[] params) {
		element.buff.buff(sender, this, duration);
	}

	public @Configurable(Configurable.Type.LOCALE) String effectBegin = "You feel you were as light as a swallow...";
	public @Configurable(Configurable.Type.LOCALE) String effectEnd = "You could feel the pull of gravity again...";
	
	public @Configurable(Configurable.Type.CONSTANT) long duration = 200;
	public @Configurable(Configurable.Type.CONSTANT) double velocity = 0.2;
	public @Configurable(Configurable.Type.STRING) String buffName = "Feather Fall";
	
	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) throws Exception {
		element.loadConfigurable(this, spellConfig);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) throws Exception {
		element.saveConfigurable(this, spellConfig);
	}

	private MagickParticle feather;
	
	@EventHandler
	public void handleFallSpeed(PlayerMoveEvent event) {
		if(!protecting.contains(event.getPlayer().getEntityId())) return;
		if(event.getPlayer().getVelocity().getY() < -velocity) {
			event.getPlayer().setVelocity(event.getPlayer().getVelocity().setY(-velocity));
			feather.play(event.getPlayer().getLocation());
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
		if(effectBegin != null) entity.sendMessage(effectBegin);
	}

	@Override
	public void remove(MagickElement element, Entity entity) {
		this.protecting.remove(entity.getEntityId());
		if(effectEnd != null) entity.sendMessage(effectEnd);
	}

	@Override
	public void after(MagickElement element) {
		element.getServer().getPluginManager().registerEvents(this, element);
		this.feather = new MagickParticle(CompatibleSound.ENDERDRAGON_WING.get(element)) {
			public float volume() {
				return Math.random() < (1.0 / 12)? 1.0f : 0.0f;
			}
		};
	}
}
