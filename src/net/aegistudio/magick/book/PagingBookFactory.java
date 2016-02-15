package net.aegistudio.magick.book;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import net.aegistudio.magick.MagickElement;

public class PagingBookFactory implements BookFactory {
	public static final String IDENTIFIER = "identifier";
	public static final String TRY_TO_CLONE = "tryToClone";
	
	String firstPageIdentifier, tryToClone;
	
	@Override
	public void setConfig(ConfigurationSection config) {
		// Written book that contains the identifier in first page considered magick book.
		firstPageIdentifier = config.getString(IDENTIFIER);
		if(firstPageIdentifier == null) {
			StringBuilder identifierBuilder = new StringBuilder();
			for(int i = 0; i < 4; i ++)
				identifierBuilder.append(Long.toHexString(
						(long) (Long.MAX_VALUE * (2.0 * Math.random() - 1))));
			
			config.set(IDENTIFIER, firstPageIdentifier = new String(identifierBuilder));
		}
		
		// Written locales.
		tryToClone = config.getString(TRY_TO_CLONE);
		if(tryToClone == null) config.set(TRY_TO_CLONE, 
				"You've copied that magick book, but it doesn't seem to have any power. Guess what's wrong?");
	}

	@Override
	public MagickBook newMagickBook(MagickElement element) {
		return new PagingMagickBook(firstPageIdentifier);
	}
	
	@Override
	public Listener newBookListener(MagickElement element) {
		return new MagickBookListener(element, tryToClone);
	}
}
