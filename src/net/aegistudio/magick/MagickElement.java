package net.aegistudio.magick;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.aegistudio.magick.book.BookFactory;
import net.aegistudio.magick.book.MagickBook;
import net.aegistudio.magick.book.PagingBookFactory;
import net.aegistudio.magick.cauldron.CauldronInventoryHandler;
import net.aegistudio.magick.cauldron.CauldronFactory;
import net.aegistudio.magick.cauldron.MagickCauldronFactory;
import net.aegistudio.magick.element.ElementHolder;
import net.aegistudio.magick.mp.MpSpellHandler;
import net.aegistudio.magick.spell.SpellHandler;
import net.aegistudio.magick.spell.SpellRegistry;

public class MagickElement extends JavaPlugin {
	public static final String BOOK_FACTORY = "bookFactory";
	public static final String BOOK_CONFIG = "bookConfig";
	public MagickBook book;
	
	public static final String CAULDRON_FACTORY = "cauldronFactory";
	public static final String CAULDRON_CONFIG = "cauldronConfig";
	public CauldronInventoryHandler cauldron;
	
	public static final String ELEMENT_CONFIG = "elementConfig";
	public ElementHolder element;
	
	public static final String HANDLER_CLASS = "handler";
	public static final String HANDLER_CONFIG = "handlerConfig";
	public SpellHandler handler;
	
	public static final String SPELL_CONFIG = "spellConfig";
	public SpellRegistry registry;

	@SuppressWarnings("unchecked")
	public void onEnable() {
		try {
			this.reloadConfig();
			FileConfiguration config = super.getConfig();

			// Book
			BookFactory bookListenerFactory = 
					this.loadInstance(BookFactory.class, config, BOOK_FACTORY, PagingBookFactory.class, BOOK_CONFIG, null);
			getLogger().info("Sucessfully loaded book configuration.");

			this.book = bookListenerFactory.newMagickBook(this);
			getLogger().info("Sucessfully installed book.");
			
			super.getServer().getPluginManager().registerEvents(bookListenerFactory.newBookListener(this), this);
			getLogger().info("Sucessfully installed book listener.");
			
			// Cauldron
			CauldronFactory cauldronFactory = 
					this.loadInstance(CauldronFactory.class, config, CAULDRON_FACTORY, MagickCauldronFactory.class, CAULDRON_CONFIG, null);
			getLogger().info("Sucessfully loaded cauldron configuration.");
			
			cauldron = cauldronFactory.newInventoryHanlder(this);
			cauldron.loadData(getDataFolder());
			getLogger().info("Sucessfully loaded cauldrons inventories.");
			
			super.getServer().getPluginManager().registerEvents(cauldronFactory.newCauldronListener(this), this);
			getLogger().info("Sucessfully installed cauldron listener.");
			
			// Element
			element = new ElementHolder();
			this.loadConfig(element, config, ELEMENT_CONFIG, new ElementInitializer());
			getLogger().info("Sucessfully loaded " + element.element.size() + " element entries,"
					+ " and " + element.transform.size() + " transforms.");
			
			// Handler
			String handlerClazz = config.getString(HANDLER_CLASS);
			if(handlerClazz == null) config.set(HANDLER_CLASS, 
					handlerClazz = MpSpellHandler.class.getName());
			handler = ((Class<? extends SpellHandler>)Class.forName(handlerClazz)).newInstance();
			if(!config.contains(HANDLER_CONFIG))
				config.createSection(HANDLER_CONFIG);
			handler.loadConfig(this, config.getConfigurationSection(HANDLER_CONFIG));
			
			// Spell
			registry = new SpellRegistry(this);
			this.loadConfig(registry, config, SPELL_CONFIG, new RegistryInitializer());
			getLogger().info("Successfully loaded " + registry.spellRegistries.size() + " spells.");
			
			super.saveConfig();
		}
		catch(Exception e) {
			getLogger().severe("Magick Element failed to load because of exception.");
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")	
	public <T extends Module> T loadInstance(Class<T> target, ConfigurationSection parent, 
			String classEntry, Class<? extends T> defaultClazz, String configEntry, Initializer<T> abscence) throws Exception {
		String classValue = parent.getString(classEntry);
		if(classValue == null) parent.set(classEntry, classValue = defaultClazz.getName());
		
		Class<T> moduleClazz = (Class<T>) Class.forName(classValue);
		T instance = moduleClazz.newInstance();
		
		this.loadConfig(instance, parent, configEntry, abscence);
		return instance;
	}
	
	public <T extends Module> T loadConfig(T instance, ConfigurationSection parent, 
			String configEntry, Initializer<T> abscence) throws Exception {
		
		ConfigurationSection config;
		if(parent.contains(configEntry) && parent.isConfigurationSection(configEntry)) {
			config = parent.getConfigurationSection(configEntry);
			instance.load(this, config);
		}
		else {
			config = parent.createSection(configEntry);
			if(abscence != null)
				abscence.initial(this, instance);
		}
		instance.save(this, config);
		return instance;
	}
	
	public void onDisable() {
		try {
			if(cauldron != null) {
				cauldron.saveData(super.getDataFolder());
				getLogger().info("Sucessfully saved cauldron inventories.");
			}
		}
		catch(Exception e) {
			getLogger().severe("Magick Element failed to save because of exception.");
			throw new RuntimeException(e);
		}
	}
}
