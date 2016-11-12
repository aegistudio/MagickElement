package net.aegistudio.magick.book;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class LoredMagickBook implements MagickBook {
	private final String bookLore;
	public LoredMagickBook(String bookLore) {
		this.bookLore = bookLore;
	}
	
	@Override
	public boolean isMagickBook(ItemStack item) {
		if(item == null) return false;
		if(item.getType() != Material.WRITTEN_BOOK) return false;
		return item.getItemMeta().getLore().contains(bookLore);
	}

	@Override
	public void makeMagickBook(ItemStack abook) {
		ArrayList<String> lores = new ArrayList<String>();
		for(String lore : abook.getItemMeta().getLore())
			lores.add(lore);
		
		lores.add(bookLore);
		abook.getItemMeta().setLore(lores);
	}

	@Override
	public void cloneMagickBook(ItemStack abook) {
		ArrayList<String> lores = new ArrayList<String>();
		for(String lore : abook.getItemMeta().getLore())
			if(!lore.equals(bookLore)) lores.add(lore);
		abook.getItemMeta().setLore(lores);
	}

	@Override
	public List<String> extractSpell(ItemStack result) {
		return ((BookMeta)(result.getItemMeta())).getPages();
	}
	
}
