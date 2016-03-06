package net.aegistudio.magick.chant;

import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.buff.Buff;
import net.aegistudio.magick.compat.CompatibleSound;
import net.aegistudio.magick.particle.PlayerParticle;
import net.aegistudio.magick.spell.SpellEntry;
import net.aegistudio.magick.spell.SpellHandler;

public class ChantSpellHandler implements SpellHandler, Buff, Listener {
	public static final String SPELL_BREAK = "spellBreak";
	public String spellBreak = "Chanting " + ChatColor.AQUA 
			+ "$magick" + ChatColor.RESET + " failed. Spell broke.";
	
	public static final String BEGIN_CHANT = "beginChant";
	public String beginChant = "Begin chanting for " + ChatColor.AQUA
			+ "$magick" + ChatColor.RESET + "...";
	
	public static final String END_CHANT = "endChant";
	public String endChant = "Finish chanting for " + ChatColor.AQUA
			+ "$magick" + ChatColor.RESET + "!";
	
	public static final String CHANTING_LIMIT = "chantLimit";
	public int chantLimit = 20;
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		beginChant = config.getString(BEGIN_CHANT);
		endChant = config.getString(END_CHANT);
		spellBreak = config.getString(SPELL_BREAK);
		if(config.contains(CHANTING_LIMIT))
			chantLimit = config.getInt(CHANTING_LIMIT);
		if(config.contains(CHANTING_BUFFNAME))
			buffName = config.getString(CHANTING_BUFFNAME);
		if(config.contains(CHANTING_INFO))
			chantingInfo = config.getString(CHANTING_INFO);
		if(config.contains(THRESHOLD))
			threshold = config.getDouble(THRESHOLD);
		if(config.contains(POINT_PER_CHANT))
			pointPerChant = config.getInt(POINT_PER_CHANT);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		config.set(BEGIN_CHANT, beginChant);
		config.set(END_CHANT, endChant);
		config.set(SPELL_BREAK, spellBreak);
		config.set(CHANTING_LIMIT, chantLimit);
		config.set(CHANTING_BUFFNAME, buffName);
		config.set(CHANTING_INFO, chantingInfo);
		config.set(THRESHOLD, threshold);
		config.set(POINT_PER_CHANT, pointPerChant);
	}

	private MagickElement element;
	@Override
	public void after(MagickElement element) {
		this.element = element;
		this.element.getServer().getPluginManager()
			.registerEvents(this, element);
	}

	@Override
	public void handleSpell(Player player, ItemStack magickBook) {
		if(!this.chantRecord.containsKey(player.getEntityId()))
			this.beginChant(player, magickBook);
		element.buff.buff(player, this, chantLimit);
	}

	public static final String CHANTING_POINT = "chantingTime";
	@Override
	public void loadSpell(SpellEntry entry, ConfigurationSection configuration) {
		int chantingTime = 0;
		if(configuration.contains(CHANTING_POINT)) 
			chantingTime = configuration.getInt(CHANTING_POINT);
		else {
			for(Integer points : entry.spellPrice.elementPoint.values())
				chantingTime += points;
			configuration.set(CHANTING_POINT, chantingTime);
		}
		entry.handlerInfo = chantingTime;
	}

	@Override
	public void saveSpell(SpellEntry entry, ConfigurationSection configuration) {
		int chantingTime = (int) entry.handlerInfo;
		configuration.set(CHANTING_POINT, chantingTime);
	}

	public static final String CHANTING_INFO = "chantingInfo";
	public String chantingInfo = ChatColor.BOLD + "Require" + ChatColor.RESET + " $required " 
			+ ChatColor.BLUE + "chanting" + ChatColor.RESET + " points.";
	@Override
	public String infoSpell(SpellEntry entry) {
		int chantingTime = (int) entry.handlerInfo;
		return chantingInfo.replace("$required", Integer.toString(chantingTime));
	}

	public static final String CHANTING_BUFFNAME = "buffName";
	public String buffName = "Chanting";
	@Override
	public String name() {
		return buffName;
	}
	
	protected void beginChant(Entity entity, ItemStack magickBook) {
		ChantRecordEntry entry = new ChantRecordEntry(element, magickBook);
		this.chantRecord.put(entity.getEntityId(), entry);
		entry.chantParticle.play(entity.getLocation());
		if(this.beginChant != null)
			entity.sendMessage(entry.tips(this.beginChant));
	}
	
	public TreeMap<Integer, ChantRecordEntry> chantRecord
		= new TreeMap<Integer, ChantRecordEntry>();
	
	public static final String POINT_PER_CHANT = "pointPerChant";
	public int pointPerChant = 5;
	
	@Override
	public void buff(MagickElement element, Entity entity) {
		int entityId = entity.getEntityId();
		if(chantRecord.containsKey(entityId)) {
			ChantRecordEntry record = chantRecord.get(entityId);
			record.chantStatus += pointPerChant;
			if(record.chantStatus >= record.chantTotal) {
				new PlayerParticle(Effect.HAPPY_VILLAGER, record.chantStatus) 
					.show(entity);
				record.chantParticle.play(entity.getLocation());
				
				// Do magick, etc
				if(entity instanceof Player) {
					Player player = (Player) entity;
					record.execute(player, element);
					if(this.endChant != null)
						record.tips(this.endChant);
				}
				chantRecord.remove(entityId);
				element.buff.unbuff(entity, this);
			}
			else 
				record.chantParticle.play(entity.getLocation());
		}
	}

	public static final String THRESHOLD = "threshold";
	public double threshold = 0.01;
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(chantRecord.containsKey(event.getPlayer().getEntityId())) {
			if(event.getTo().distance(event.getFrom()) >= threshold) 
				element.buff.unbuff(event.getPlayer(), this);
		}
	}
	
	@Override
	public void remove(MagickElement element, Entity entity) {
		ChantRecordEntry record = chantRecord.remove(entity.getEntityId());
		if(record != null) {
			if(spellBreak != null)
				entity.sendMessage(record.tips(spellBreak));
			entity.getWorld().playSound(entity.getLocation(), CompatibleSound.NOTE_BASS.get(element), 1.2f, 0.5f);
			entity.getWorld().playSound(entity.getLocation(), CompatibleSound.NOTE_BASS.get(element), 1.2f, 0.25f);
		}
	}
}
