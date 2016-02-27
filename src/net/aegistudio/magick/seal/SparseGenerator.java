package net.aegistudio.magick.seal;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.bukkit.configuration.ConfigurationSection;

import net.aegistudio.magick.MagickElement;

public class SparseGenerator implements Generator {
	@Override
	public void generate(Painter painter) {
		for(int i = 0; i < x.length; i ++)
			painter.paint(x[i], y[i], 0);
	}

	private Filter filter = new GrayScaleFilter();
	private Double[] x, y;
	public void parse(Raster raster) {
		int w = raster.getWidth();
		int h = raster.getHeight();
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();
		
		for(int i = 0; i < w; i += resolution)
			for(int j = 0; j < h; j += resolution)
				if(filter.pick(raster, i, j)) {
					x.add(2.0 * i / w - 1.0);
					y.add(2.0 * j / h - 1.0);
				}
		
		this.x = x.toArray(new Double[0]);
		this.y = y.toArray(new Double[0]);
	}

	private int resolution = 1;
	public static final String RESOLUTION = "resolution";
	
	
	private File source;
	public static final String SOURCE_IMAGE = "source";
	public static final String FILTER_CLAZZ = "filterClass";
	public static final String FILTER_CONFIG = "filterConfig";
	
	@Override
	public void load(MagickElement magick, ConfigurationSection config) throws Exception {
		if(config.contains(RESOLUTION)) resolution = config.getInt(RESOLUTION);
		source = new File(magick.getDataFolder(), config.getString(SOURCE_IMAGE));
		filter = magick.loadInstance(Filter.class, config, FILTER_CLAZZ, GrayScaleFilter.class, FILTER_CONFIG, null);

		if(source.exists()) {
			BufferedImage image = ImageIO.read(source);
			this.parse(image.getRaster());
		}
	}

	@Override
	public void save(MagickElement element, ConfigurationSection config) throws Exception {
		config.set(RESOLUTION, resolution);
		config.set(SOURCE_IMAGE, source != null? source.getName() : "<image-name>");
		config.set(FILTER_CLAZZ, filter.getClass().getName());
		filter.save(element, config.createSection(FILTER_CONFIG));
	}

	@Override
	public void after(MagickElement element) {	
		filter.after(element);
	}
}
