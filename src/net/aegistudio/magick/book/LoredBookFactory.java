package net.aegistudio.magick.book;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import net.aegistudio.magick.MagickElement;
import net.md_5.bungee.api.ChatColor;

public class LoredBookFactory implements BookFactory {
	public static final String BOOK_LORE = "bookLore";
	public static final String TRY_TO_CLONE = "tryToClone";
	
	protected String bookLore = ChatColor.MAGIC + "Magick Book";
	protected String tryToClone = "You've copied that magick book, but it doesn't seem to have any power. Guess what's wrong?";
	
	@Override
	public void setConfig(ConfigurationSection config) {
		// Written book that contains the identifier in first page considered magick book.

		if(config.contains(BOOK_LORE)) 
			bookLore = config.getString(BOOK_LORE);
		config.set(BOOK_LORE, bookLore);
		
		// Written locales.
		if(config.contains(TRY_TO_CLONE))
			tryToClone = config.getString(TRY_TO_CLONE);
		config.set(TRY_TO_CLONE, tryToClone);
	}

	@Override
	public MagickBook newMagickBook(MagickElement element) {
		return new LoredMagickBook(bookLore);
	}
	
	@Override
	public Listener newBookListener(MagickElement element) {
		return new MagickBookListener(element, tryToClone);
	}
}
