package net.aegistudio.magick.effect;

import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.aegistudio.magick.AlgebraExpression;
import net.aegistudio.magick.Configurable;
import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Parameter;
import net.aegistudio.magick.buff.Buff;
import net.aegistudio.magick.spell.SpellEffect;

public class VampireHand implements SpellEffect, Buff, Listener {
	public TreeMap<Integer, Double> vampire = new TreeMap<Integer, Double>();
	public @Configurable(Configurable.Type.ALGEBRA) AlgebraExpression duration = new AlgebraExpression("200");
	public @Configurable(Configurable.Type.ALGEBRA) AlgebraExpression adsorption = new AlgebraExpression(".5");
	
	public @Configurable(Configurable.Type.LOCALE) String beginVampire = "You've grown a pair of nails teeth...";
	public @Configurable(Configurable.Type.LOCALE) String endVampire = "You've regain your sanity...";
	public @Configurable(Configurable.Type.STRING) String buffName = "Vampire Hand";
	
	@Override
	public void spell(MagickElement element, Entity sender, Location location, String[] params) {
		element.buff.buff(sender, this, duration.getInt(new Parameter(params)), params);
	}

	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) throws Exception {
		element.loadConfigurable(this, spellConfig);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) throws Exception {
		element.saveConfigurable(this, spellConfig);
	}

	@Override
	public String name() {
		return buffName;
	}

	@Override
	public void buff(MagickElement element, Entity entity, String[] params) {
		vampire.put(entity.getEntityId(), adsorption.getDouble(new Parameter(params)));
		if(beginVampire != null) entity.sendMessage(beginVampire);
	}

	@Override
	public void remove(MagickElement element, Entity entity) {
		vampire.remove(entity.getEntityId());
		if(endVampire != null) entity.sendMessage(endVampire);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if(this.vampire.containsKey(event.getDamager().getEntityId())) {
			Entity damager = event.getDamager();
			if(!(damager instanceof LivingEntity)) return; 
			LivingEntity vampire = (LivingEntity) damager;
			double healCount = event.getDamage() * this.vampire.get(event.getDamager().getEntityId());
			vampire.setHealth(Math.min(vampire.getHealth() + healCount, vampire.getMaxHealth()));
		}
	}

	@Override
	public void after(MagickElement element) {
		element.getServer().getPluginManager().registerEvents(this, element);
	}
}
