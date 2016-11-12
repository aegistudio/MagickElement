package net.aegistudio.magick.inventory;

import org.bukkit.Location;
import org.bukkit.World;

public class BlockCoordinate implements Comparable<BlockCoordinate> {
	public final World w;
	public final int x, y, z;
	public BlockCoordinate(Location l) {
		this.w = l.getWorld();
		this.x = l.getBlockX();
		this.y = l.getBlockY();
		this.z = l.getBlockZ();
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof BlockCoordinate)) return false;
		BlockCoordinate coord = (BlockCoordinate) obj;
		if(!w.getUID().equals(coord.w.getUID())) return false;
		return (x == coord.x && y == coord.y && z == coord.z);
	}

	@Override
	public int compareTo(BlockCoordinate o) {
		if(this.equals(o)) return 0;
		
		int wComp = o.w.getUID().compareTo(w.getUID());
		if(wComp != 0) return wComp;
		
		if(this.x > o.x) return 1;
		if(this.y > o.y) return 1;
		if(this.z > o.z) return 1;
		return -1;
	}
}
