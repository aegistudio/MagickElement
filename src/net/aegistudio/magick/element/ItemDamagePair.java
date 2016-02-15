package net.aegistudio.magick.element;

import org.bukkit.Material;

public class ItemDamagePair implements Comparable<ItemDamagePair>{
	public final Material material;
	public final int damage;
	
	public ItemDamagePair(Material material, int damage) {
		this.material = material;
		this.damage = damage;
	}

	@Override
	public int compareTo(ItemDamagePair target) {
		int materialRelation = (material.compareTo(material));
		if(materialRelation != 0) return materialRelation;
		if(damage == target.damage) return 0;
		return damage > target.damage? 1 : -1;
	}
	
	public static ItemDamagePair parse(String input) {
		String[] list = input.split(":");
		Material material = Material.getMaterial(list[0]);
		if(material == null) return null;
		int damage = -1;
		if(list.length > 1) 
			damage = Integer.parseInt(list[1]);
		
		return new ItemDamagePair(material, damage);
	}
	
	public String toString() {
		if(damage != -1) return material.toString() + ":" + damage;
		else return material.toString();
	}
}
