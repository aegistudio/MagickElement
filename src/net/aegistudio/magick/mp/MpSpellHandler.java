package net.aegistudio.magick.mp;

import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.spell.SpellEntry;
import net.aegistudio.magick.spell.SpellHandler;

/**
 * Consume mana power (MP) to use spells. MP is calculated
 * from the current exp level of a player.
 * @author aegistudio
 */

public class MpSpellHandler implements SpellHandler {
	public final TreeMap<String, Integer> mp = new TreeMap<String, Integer>();
	public final TreeSet<String> cooling = new TreeSet<String>();
	
	@Override
	public void handleSpell(Player player, ItemStack magickBook) {
		if(cooling.contains(player.getName()))
			player.sendMessage(cooldownMessage);
		else {
			List<String> spells = element.book.extractSpell(magickBook);
			int spellPoint = 0;
			for(String spell : spells) 
				spellPoint += (int)(element.registry.getSpell(spell).handlerInfo);
			
			String finalMessage = insufficient;
			if(spellPoint <= getMp(player)) {
				finalMessage = sufficient;
				this.addMp(player, - spellPoint);
				
				for(String spell : spells)
					element.registry.getSpell(spell)
						.makeMagick(element, player, player.getLocation(), magickBook, spell);
				
				new MpCooldown(this, player);
			}
			
			String title = ((BookMeta)(magickBook.getItemMeta())).getTitle();
			int actual = getMp(player);
			int total = getMaxMp(player);
			
			finalMessage = finalMessage.replace("$magick", title)
					.replace("$actual", Integer.toString(actual))
					.replace("$total", Integer.toString(total))
					.replace("$required", Integer.toString(spellPoint));
			
			if(finalMessage.length() > 0)
				player.sendMessage(finalMessage);
		}
	}

	public static final String MP_COST = "mpCost";
	@Override
	public void loadSpell(SpellEntry entry, ConfigurationSection configuration) {
		int mpCost = 0;
		if(configuration.contains(MP_COST))
			mpCost = configuration.getInt(MP_COST);
		else {
			for(Integer count : entry.spellPrice.elementPoint.values())
				if(count != null) mpCost += count;
		}
		entry.handlerInfo = mpCost;
	}

	@Override
	public void saveSpell(SpellEntry entry, ConfigurationSection configuration) {
		int mpCost = (int) entry.handlerInfo;
		configuration.set(MP_COST, mpCost);
	}
	
	public int getMp(Player player) {
		Integer mpValue = mp.get(player.getName());
		if(mpValue == null) {
			mpValue = 0;
			mp.put(player.getName(), mpValue);
		}
		return mpValue;
	}
	
	public int getMaxMp(Player player) {
		return base + player.getLevel() * multiplier;		
	}
	
	public void setMp(Player player, int mpValue) {
		int maxMp = this.getMaxMp(player);
		if(mpValue > maxMp) mpValue = maxMp;
		
		mp.put(player.getName(), mpValue);
	}
	
	public void addMp(Player player, int mpValue) {
		setMp(player, getMp(player) + mpValue);
	}
	
	MagickElement element;
	
	public static final String RECOVERY_INTERVAL = "recoveryInterval";
	int recoveryCount = 1;
	
	public static final String RECOVERY_COUNT = "recoveryCount";
	long recoveryInterval = 100;	

	public static final String ONLINE_ONLY = "onlineOnly";
	boolean onlineOnly = true;

	public static final String MP_BASE = "base";
	int base = 5;
	
	public static final String MP_MULTIPLIER = "multiplier";
	int multiplier = 3;
	
	public static final String MP_COOLDOWN = "cooldown";
	public static final String MP_COOLDOWN_MESSAGE = "cooldownMessage";
	int cooldown = 20;
	String cooldownMessage = "You can't use your magick cause you're cooling down.";
	
	public static final String MP_INSUFFICIENT = "insufficient";
	String insufficient = "You dont have enough mana power to use $magick: $actual(+$required)/$total";
	
	public static final String MP_SUFFICIENT = "sufficient";
	String sufficient = "$magick used successfully, current status: $actual(-$required)/$total";	
	
	@Override
	public void loadConfig(MagickElement element, ConfigurationSection configuration) {
		this.element = element;
		
		if(configuration.contains(RECOVERY_COUNT)) 
			recoveryCount = configuration.getInt(RECOVERY_COUNT);
		configuration.set(RECOVERY_COUNT, recoveryCount);
		
		if(configuration.contains(RECOVERY_INTERVAL))
			recoveryInterval = configuration.getInt(RECOVERY_INTERVAL);
		configuration.set(RECOVERY_INTERVAL, recoveryInterval);
		
		if(configuration.contains(ONLINE_ONLY))
			onlineOnly = configuration.getBoolean(ONLINE_ONLY);
		configuration.set(ONLINE_ONLY, onlineOnly);
		
		if(configuration.contains(MP_BASE))
			base = configuration.getInt(MP_BASE);
		configuration.set(MP_BASE, base);
		
		if(configuration.contains(MP_MULTIPLIER))
			multiplier = configuration.getInt(MP_MULTIPLIER);
		configuration.set(MP_MULTIPLIER, multiplier);
		
		element.getServer().getScheduler().scheduleSyncRepeatingTask(element, 
				new MpRecovery(element, this)
				, recoveryInterval, recoveryInterval);
		
		if(configuration.contains(MP_COOLDOWN))
			cooldown = configuration.getInt(MP_COOLDOWN);
		configuration.set(MP_COOLDOWN, cooldown);
		
		if(configuration.contains(MP_COOLDOWN_MESSAGE))
			cooldownMessage = configuration.getString(MP_COOLDOWN_MESSAGE);
		configuration.set(MP_COOLDOWN_MESSAGE, cooldownMessage);
		
		if(configuration.contains(MP_INSUFFICIENT))
			insufficient = configuration.getString(MP_INSUFFICIENT);
		configuration.set(MP_INSUFFICIENT, insufficient);
		
		if(configuration.contains(MP_SUFFICIENT))
			sufficient = configuration.getString(MP_SUFFICIENT);
		configuration.set(MP_SUFFICIENT, sufficient);
	}
}
