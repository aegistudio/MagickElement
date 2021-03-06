package net.aegistudio.magick.seal;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.spell.SpellEffect;

public class MagickSealEffect implements SpellEffect {
	Generator generator;
	private static final String GENERATOR_CLAZZ = "generatorClass";
	private static final String GENERATOR_CONFIG = "generatorConfig";
	
	PainterFactory painterFactory;
	private static final String PAINTER_CLAZZ = "painterClass";
	private static final String PAINTER_CONFIG = "painterConfig";
	
	int totalPaintTick = 20;
	private static final String TOTAL_PAINT_TICK = "totalPaintTick";
	
	long repaintTick = 3;
	private static final String REPAINT_TICK = "repaintTick";
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		if(config.contains(TOTAL_PAINT_TICK)) totalPaintTick = config.getInt(TOTAL_PAINT_TICK);
		if(config.contains(REPAINT_TICK)) repaintTick = config.getInt(REPAINT_TICK);
		
		painterFactory = magick.loadInstance(PainterFactory.class, config, 
				PAINTER_CLAZZ, TrackedFactory.class, PAINTER_CONFIG, null);
		generator = magick.loadInstance(Generator.class, config, GENERATOR_CLAZZ, 
				SparseGenerator.class, GENERATOR_CONFIG, null);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		config.set(TOTAL_PAINT_TICK, totalPaintTick);
		config.set(REPAINT_TICK, repaintTick);
		
		element.saveInstance(painterFactory, config, PAINTER_CLAZZ, PAINTER_CONFIG);
		element.saveInstance(generator, config, GENERATOR_CLAZZ, GENERATOR_CONFIG);
	}

	MagickElement element;
	@Override
	public void after(MagickElement element) {
		this.element = element;
		
		this.painterFactory.after(element);
		this.generator.after(element);
	}
	
	@Override
	public void spell(MagickElement element, Entity sender, Location location, String[] params) {
		new MagickSealPaintTask(this, painterFactory.newPainter(sender), params).start();
	}
}
