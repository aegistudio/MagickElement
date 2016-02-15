package net.aegistudio.magick.cauldron;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import net.aegistudio.magick.MagickElement;

public class MagickCauldronFactory implements CauldronFactory {
	public static final String CAPACITY = "capacity";
	private int cauldronCapacity = 2;
	
	public static final String BEGIN_FORGE = "beginForging";
	private String beginForging = "Cauldron begins to forge magick elements!";
	
	public static final String STILL_FORGE = "stillForging";
	private String stillForging = "Cauldron is still forging magick elements.";
	
	public static final String TICK_INTERVAL = "tickInterval";
	private int tickInterval = 20;
	
	public static final String TICK_FORGE = "tickToForge";
	private int tickForge = 5;
	
	public static final String CAULDRON_TITLE = "cauldronTitle";
	private String cauldronTitle = "Magick Cauldron";
	
	@Override
	public void loadConfig(ConfigurationSection config) {
		if(config.contains(CAPACITY))
			cauldronCapacity = config.getInt(CAPACITY);
		config.set(CAPACITY, cauldronCapacity);
		
		if(config.contains(BEGIN_FORGE))
			beginForging = config.getString(BEGIN_FORGE);
		config.set(BEGIN_FORGE, beginForging);
		
		if(config.contains(STILL_FORGE))
			stillForging = config.getString(STILL_FORGE);
		config.set(STILL_FORGE, stillForging);
		
		if(config.contains(TICK_FORGE))
			tickForge = config.getInt(TICK_FORGE);
		config.set(TICK_FORGE, tickForge);

		if(config.contains(TICK_INTERVAL))
			tickInterval = config.getInt(TICK_INTERVAL);
		config.set(TICK_INTERVAL, tickInterval);
		
		if(config.contains(CAULDRON_TITLE))
			cauldronTitle = config.getString(CAULDRON_TITLE);
		config.set(CAULDRON_TITLE, cauldronTitle);
	}

	@Override
	public Listener newCauldronListener(MagickElement element) {
		return new MagickCauldronListener(element, element.cauldron, 
				beginForging, stillForging, tickInterval, tickForge);
	}

	@Override
	public CauldronInventoryHandler newInventoryHanlder(MagickElement element) {
		return new CauldronInventoryHandler(element, cauldronCapacity, cauldronTitle);
	}

}
