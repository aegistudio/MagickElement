package net.aegistudio.magick.effect;

import java.util.TreeSet;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.buff.Buff;
import net.aegistudio.magick.spell.SpellEffect;

public class VampireHand implements SpellEffect, Buff, Listener {
	public TreeSet<Integer> vampire = new TreeSet<Integer>();
	public static final String DURATION = "duration"; public int duration = 200;
	public static final String ADSORPTION = "adsorption"; public double adsorption = .5;
	
	public static final String BEGIN_VAMPIRE = "beginVampire";
	public String beginVampire = "You've grown a pair of nails teeth...";
	public static final String END_VAMPIRE = "endVampire";
	public String endVampire = "You've regain your sanity...";
	public static final String VAMPIRE_BUFFNAME = "buffName";
	public String buffName = "Vampire Hand";
	
	@Override
	public void spell(MagickElement element, Entity sender, Location location, String[] params) {
		element.buff.buff(sender, this, duration);
	}

	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) {
		if(spellConfig.contains(DURATION)) duration = spellConfig.getInt(DURATION);
		if(spellConfig.contains(ADSORPTION)) adsorption = spellConfig.getDouble(ADSORPTION);
		beginVampire = spellConfig.getString(BEGIN_VAMPIRE);
		endVampire = spellConfig.getString(END_VAMPIRE);
		if(spellConfig.contains(VAMPIRE_BUFFNAME)) 
			buffName = spellConfig.getString(VAMPIRE_BUFFNAME);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) {
		spellConfig.set(DURATION, duration);
		spellConfig.set(ADSORPTION, adsorption);
		spellConfig.set(BEGIN_VAMPIRE, beginVampire);
		spellConfig.set(END_VAMPIRE, endVampire);
		spellConfig.set(VAMPIRE_BUFFNAME, buffName);
	}

	@Override
	public String name() {
		return buffName;
	}

	@Override
	public void buff(MagickElement element, Entity entity) {
		vampire.add(entity.getEntityId());
		if(beginVampire != null) entity.sendMessage(beginVampire);
	}

	@Override
	public void remove(MagickElement element, Entity entity) {
		vampire.remove(entity.getEntityId());
		if(endVampire != null) entity.sendMessage(endVampire);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if(this.vampire.contains(event.getDamager().getEntityId())) {
			Entity damager = event.getDamager();
			if(!(damager instanceof LivingEntity)) return; 
			LivingEntity vampire = (LivingEntity) damager;
			double healCount = event.getDamage() * adsorption;
			vampire.setHealth(Math.min(vampire.getHealth() + healCount, vampire.getMaxHealth()));
		}
	}

	@Override
	public void after(MagickElement element) {
		element.getServer().getPluginManager().registerEvents(this, element);
	}
}
