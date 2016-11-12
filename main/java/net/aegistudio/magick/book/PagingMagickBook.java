package net.aegistudio.magick.book;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class PagingMagickBook implements MagickBook {
	private final String firstPageContent;
	
	public PagingMagickBook(String firstPageContent) {
		this.firstPageContent = firstPageContent;
	}
	
	/** Check whehter it is a magick book. **/
	@Override
	public boolean isMagickBook(ItemStack item) {
		if(item == null) return false;
		if(item.getType() != Material.WRITTEN_BOOK) return false;
		BookMeta metadata = (BookMeta) item.getItemMeta();
		if(metadata.getPageCount() < 1) return false;
		return metadata.getPage(1).equals(firstPageContent);
	}

	/** Craft a book-with spells into a magick book **/
	@Override
	public void makeMagickBook(ItemStack abook) {
		BookMeta book = (BookMeta) abook.getItemMeta();
		ArrayList<String> pages = new ArrayList<String>(book.getPages());
		pages.add(0, firstPageContent);
		book.setPages(pages);
		abook.setItemMeta(book);
	}

	/** Clone the magick book. **/
	@Override
	public void cloneMagickBook(ItemStack clonedBook) {
		BookMeta metadata = (BookMeta) clonedBook.getItemMeta();
		
		// Removing identifier and lore.
		ArrayList<String> pages = new ArrayList<String>(metadata.getPages());
		pages.remove(0); metadata.setPages(pages);
		metadata.setLore(null);
		
		// Writing back to event.
		clonedBook.setItemMeta(metadata);
	}

	@Override
	public List<String> extractSpell(ItemStack result) {
		BookMeta book = (BookMeta) result.getItemMeta();
		ArrayList<String> spell = new ArrayList<String>();
		for(int i = 2; i <= book.getPageCount(); i ++)
			spell.add(book.getPage(i));
		return spell;
	}
}
