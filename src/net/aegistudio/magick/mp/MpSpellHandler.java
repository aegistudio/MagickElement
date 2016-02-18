package net.aegistudio.magick.mp;

import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.particle.PlayerParticle;
import net.aegistudio.magick.spell.SpellEntry;
import net.aegistudio.magick.spell.SpellHandler;
import net.md_5.bungee.api.ChatColor;

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
				new PlayerParticle(Effect.HAPPY_VILLAGER, spellPoint).show(player);
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
	String insufficient = "You dont have enough mana power to use " + ChatColor.AQUA + "$magick" + ChatColor.RESET
			+ ": " + ChatColor.BLUE + "$actual" + ChatColor.RESET + "(" + ChatColor.RED + "+$required"
			+ ChatColor.RESET + ")/" + ChatColor.BLUE + "$total" + ChatColor.RESET;
	
	public static final String MP_SUFFICIENT = "sufficient";
	String sufficient = "The " + ChatColor.AQUA + "$magick" + ChatColor.RESET + " used successfully, current status: " 
			+ ChatColor.BLUE + "$actual" + ChatColor.RESET + "(" 
			+ ChatColor.GREEN + "-$required" + ChatColor.RESET + ")/" + ChatColor.BLUE + "$total" + ChatColor.RESET;	
	
	public static final String MP_CONSUME = "mpConsume";
	String mpConsume = ChatColor.BOLD + "Consume" + ChatColor.RESET + " $required " + ChatColor.BLUE + "mp" + ChatColor.RESET + ".";
	
	@Override
	public void load(MagickElement element, ConfigurationSection configuration) {
		if(configuration.contains(RECOVERY_COUNT)) 
			recoveryCount = configuration.getInt(RECOVERY_COUNT);
		if(configuration.contains(RECOVERY_INTERVAL))
			recoveryInterval = configuration.getInt(RECOVERY_INTERVAL);
		if(configuration.contains(ONLINE_ONLY))
			onlineOnly = configuration.getBoolean(ONLINE_ONLY);
		if(configuration.contains(MP_BASE))
			base = configuration.getInt(MP_BASE);
		if(configuration.contains(MP_MULTIPLIER))
			multiplier = configuration.getInt(MP_MULTIPLIER);
		if(configuration.contains(MP_COOLDOWN))
			cooldown = configuration.getInt(MP_COOLDOWN);
		if(configuration.contains(MP_COOLDOWN_MESSAGE))
			cooldownMessage = configuration.getString(MP_COOLDOWN_MESSAGE);
		if(configuration.contains(MP_INSUFFICIENT))
			insufficient = configuration.getString(MP_INSUFFICIENT);
		if(configuration.contains(MP_SUFFICIENT))
			sufficient = configuration.getString(MP_SUFFICIENT);
		if(configuration.contains(MP_CONSUME))
			mpConsume = configuration.getString(MP_CONSUME);
		
		element.getServer().getScheduler().scheduleSyncRepeatingTask(element, 
				new MpRecovery(element, this)
				, recoveryInterval, recoveryInterval);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection configuration) throws Exception {
		configuration.set(RECOVERY_COUNT, recoveryCount);
		configuration.set(RECOVERY_INTERVAL, recoveryInterval);
		configuration.set(ONLINE_ONLY, onlineOnly);
		configuration.set(MP_BASE, base);
		configuration.set(MP_MULTIPLIER, multiplier);
		configuration.set(MP_COOLDOWN, cooldown);
		configuration.set(MP_COOLDOWN_MESSAGE, cooldownMessage);
		configuration.set(MP_INSUFFICIENT, insufficient);
		configuration.set(MP_SUFFICIENT, sufficient);
		configuration.set(MP_CONSUME, mpConsume);
	}

	@Override
	public String infoSpell(SpellEntry entry) {
		return mpConsume.replace("$required", Integer.toString((Integer)entry.handlerInfo));
	}

	@Override
	public void after(MagickElement element) {
		this.element = element;
	}
}
