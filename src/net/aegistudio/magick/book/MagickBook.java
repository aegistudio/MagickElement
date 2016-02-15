package net.aegistudio.magick.book;

import java.util.List;

import org.bukkit.inventory.ItemStack;

/**
 * In charge of validate, entrypting and decrypting magick book.
 * @author aegistudio
 */

public interface MagickBook {
	public boolean isMagickBook(ItemStack abook);

	public void makeMagickBook(ItemStack abook);

	public void cloneMagickBook(ItemStack result);
	
	public List<String> extractSpell(ItemStack result);
}
