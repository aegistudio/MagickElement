package net.aegistudio.magick.book;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import net.aegistudio.magick.MagickElement;

public class PagingBookFactory implements BookFactory {
	public static final String IDENTIFIER = "identifier";
	public static final String TRY_TO_CLONE = "tryToClone";
	
	String firstPageIdentifier; {
		StringBuilder identifierBuilder = new StringBuilder();
		for(int i = 0; i < 4; i ++)
			identifierBuilder.append(Long.toHexString(
					(long) (Long.MAX_VALUE * (2.0 * Math.random() - 1))));
		firstPageIdentifier = new String(identifierBuilder);
	}
	String tryToClone = "You've copied that magick book, but it doesn't seem to have any power. Guess what's wrong?";
	
	@Override
	public MagickBook newMagickBook(MagickElement element) {
		return new PagingMagickBook(firstPageIdentifier);
	}
	
	@Override
	public Listener newBookListener(MagickElement element) {
		return new MagickBookListener(element, tryToClone);
	}

	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		if(config.contains(TRY_TO_CLONE)) tryToClone = config.getString(TRY_TO_CLONE);
		if(config.contains(IDENTIFIER)) firstPageIdentifier = config.getString(IDENTIFIER);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		config.set(TRY_TO_CLONE, tryToClone);
		config.set(IDENTIFIER, firstPageIdentifier);
	}
}
