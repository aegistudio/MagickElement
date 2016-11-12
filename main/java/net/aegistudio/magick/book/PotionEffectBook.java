package net.aegistudio.magick.book;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class PotionEffectBook implements MagickBook {
	public PotionEffectBook(int potionEffectId) {
		
	}

	@Override
	public boolean isMagickBook(ItemStack abook) {
		return false;
	}

	@Override
	public void makeMagickBook(ItemStack abook) {
		
	}

	@Override
	public void cloneMagickBook(ItemStack result) {
		
	}

	@Override
	public List<String> extractSpell(ItemStack result) {
		return null;
	}
}
