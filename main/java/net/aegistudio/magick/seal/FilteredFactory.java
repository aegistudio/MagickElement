package net.aegistudio.magick.seal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;

public class FilteredFactory extends WrappedPainterFactory {
	protected int scanInterval = 3;
	public static final String SCAN_INTERVAL = "scanInterval";
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		super.load(magick, config);
		if(config.contains(SCAN_INTERVAL)) this.scanInterval = config.getInt(SCAN_INTERVAL);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		super.save(element, config);
		config.set(SCAN_INTERVAL, scanInterval);
	}

	@Override
	public Painter newPainter(Entity entity) {
		return new FilteredPainter(this.wrapped.newPainter(entity), scanInterval);
	}
}
