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
		element.saveInstance(this.wrapped, config, WRAPPED_CLASS, WRAPPED_CONFIG);
	}

	@Override
	public void after(MagickElement element) {
		this.wrapped.after(element);
	}
}
