package net.aegistudio.magick.buff;

import java.util.HashMap;

public class BuffRecord {
	public HashMap<Buff, Long> buffRecord = new HashMap<Buff, Long>();
	
	public void set(Buff buff, long duration) {
		if(duration <= 0) buffRecord.remove(buff);
		else buffRecord.put(buff, duration);
	}
	
	public long get(Buff buff) {
		Long buffCount = buffRecord.get(buff);
		if(buffCount == null) return 0;
		else return buffCount;
	}
	
	public void increment(Buff buff, int count) {
		this.set(buff, this.get(buff) + count);
	}
	
	public boolean hasBuff(Buff buff) {
		return buffRecord.containsKey(buff);
	}
	
	public boolean hasBuff() {
		return !buffRecord.isEmpty();
	}
}
