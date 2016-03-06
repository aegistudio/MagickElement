package net.aegistudio.magick.buff;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;

public class ScoreboardBuffManager implements BuffManager {
	final HashMap<Entity, ScoreboardBuffRecord> entityBuffs 
		= new HashMap<Entity, ScoreboardBuffRecord>();
	
	MagickElement element;
	long updateTick = 2;
	String scoreboardTitle = ChatColor.YELLOW + "Buff";
	
	private ScoreboardBuffUpdater updater = new ScoreboardBuffUpdater(this);
	
	@Override
	public void buff(Entity entity, Buff buff, long duration) {
		ScoreboardBuffRecord record = entityBuffs.get(entity);
		if(record == null) entityBuffs.put(entity, 
				record = new ScoreboardBuffRecord(element.getServer()
						.getScoreboardManager().getNewScoreboard(), scoreboardTitle, updateTick));
		
		record.set(buff, duration);
		buff.buff(element, entity);
		updater.start();
	}

	public static final String UPDATE_TICK = "updateTick";
	public static final String SCOREBOARD_TITLE = "scoreboardTitle";
	@Override
	public void load(MagickElement element, ConfigurationSection section) {
		if(section.contains(UPDATE_TICK))
			updateTick = section.getLong(UPDATE_TICK);
		if(section.contains(SCOREBOARD_TITLE))
			scoreboardTitle = section.getString(SCOREBOARD_TITLE);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection section) {
		section.set(UPDATE_TICK, updateTick);
		section.set(SCOREBOARD_TITLE, scoreboardTitle);
	}
	
	@Override
	public void after(MagickElement element) {
		this.element = element;
	}

	@Override
	public void unbuff(Entity entity, Buff buff) {
		BuffRecord buffRecord = entityBuffs.get(entity);
		if(buffRecord == null) return;
		if(buffRecord.hasBuff(buff)) {
			buffRecord.remove(buff);
			buff.remove(element, entity);
		}
	}
}
