package net.aegistudio.magick.buff;

import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;

public class MutedBuffManager implements BuffManager {
	private final TreeMap<Integer, BuffRecord> entityBuffs = new TreeMap<Integer, BuffRecord>();
	
	class BuffRemoval implements Runnable {
		private final BuffRecord record;
		private final Buff buff;
		private final Entity entity;
		public BuffRemoval(Entity entity, BuffRecord record, Buff buff) {
			this.record = record;
			this.buff = buff;
			this.entity = entity;
		}
		@Override
		public void run() {
			this.record.increment(buff, -1);
			if(!this.record.hasBuff(buff)) 
				buff.remove(element, entity);
			
			if(this.record.hasBuff())
				entityBuffs.remove(entity.getEntityId());
		}
	}
	
	@Override
	public void buff(Entity entity, Buff buff, long duration) {
		BuffRecord buffRecord = entityBuffs.get(entity.getEntityId());
		if(buffRecord == null) entityBuffs.put(entity.getEntityId(), 
				buffRecord = new BuffRecord());
		
		buffRecord.increment(buff, 1);
		buff.buff(element, entity);
		element.getServer().getScheduler().runTaskLater(element, 
				new BuffRemoval(entity, buffRecord, buff), duration);
	}
	
	private MagickElement element;
	
	@Override
	public void load(MagickElement element, ConfigurationSection section) {
		this.element = element;
	}

	@Override
	public void save(MagickElement element, ConfigurationSection section) {
		this.element = element;
	}
	
}
