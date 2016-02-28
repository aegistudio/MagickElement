package net.aegistudio.magick.seal;

import org.bukkit.configuration.ConfigurationSection;
import net.aegistudio.magick.MagickElement;

public abstract class WrappedPainterFactory implements PainterFactory{
	protected PainterFactory wrapped = new TrackedFactory();	
	public static final String WRAPPED_CLASS = "wrappedClass";
	public static final String WRAPPED_CONFIG = "wrappedConfig";
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		this.wrapped = magick.loadInstance(PainterFactory.class, config, WRAPPED_CLASS, 
				TrackedFactory.class, WRAPPED_CONFIG, null);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		config.set(WRAPPED_CLASS, this.wrapped.getClass().getName());
		if(!config.contains(WRAPPED_CONFIG)) config.createSection(WRAPPED_CONFIG);
		this.wrapped.save(element, config.getConfigurationSection(WRAPPED_CONFIG));
	}

	@Override
	public void after(MagickElement element) {
		this.wrapped.after(element);
	}
}
