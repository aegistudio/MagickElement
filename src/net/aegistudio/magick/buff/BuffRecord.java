package net.aegistudio.magick.buff;

import java.util.HashMap;

public class BuffRecord {
	public HashMap<Buff, Integer> buffRecord = new HashMap<Buff, Integer>();
	
	public void set(Buff buff, int count) {
		if(count == 0) buffRecord.remove(buff);
		else buffRecord.put(buff, count);
	}
	
	public int get(Buff buff) {
		Integer buffCount = buffRecord.get(buff);
		if(buffCount == null) return 0;
		else return buffCount;
	}
	
	public void increment(Buff buff, int count) {
		this.set(buff, this.get(buff) + count);
	}
	
	public boolean hasBuff(Buff buff) {
		return buffRecord.containsKey(buff);
	}
}
