package net.aegistudio.magick;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

import net.aegistudio.magick.book.BookFactory;
import net.aegistudio.magick.book.MagickBook;
import net.aegistudio.magick.book.PagingBookFactory;
import net.aegistudio.magick.buff.BuffManager;
import net.aegistudio.magick.buff.ScoreboardBuffManager;
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
	
	public TreeMap<String, CommandHandle> commands;
	
	public static final String BUFFMANAGER_CLASS = "buffManager";
	public static final String BUFFMANAGER_CONFIG = "buffConfig";
	public BuffManager buff;
	
	public static final String MAPC_PATH = "mapc.properties";
	public TreeMap<String, Class<?>> abbrClassTable;
	public Class<?> lookForClass(String name) throws ClassNotFoundException {
		Class<?> clazz = abbrClassTable.get(name);
		return (clazz == null)? Class.forName(name) : clazz;
	}
	
	public HashMap<Class<?>, String> classAbbrTable;
	public String lookForName(Class<?> clazz) {
		String className = classAbbrTable.get(clazz);
		return (className == null)? clazz.getName() : className;
	}
	
	public void onEnable() {
		try {
			this.reloadConfig();
			FileConfiguration config = super.getConfig();
			commands = new TreeMap<String, CommandHandle>();

			// ClassTable
			abbrClassTable = new TreeMap<String, Class<?>>();
			classAbbrTable = new HashMap<Class<?>, String>();
			super.saveResource(MAPC_PATH, false);
			
			Properties mapc = new Properties();
			mapc.load(new FileInputStream(new File(super.getDataFolder(), MAPC_PATH)));
			for(Entry<Object, Object> e : mapc.entrySet()) {
				String key = (String) e.getKey();
				Class<?> value = Class.forName((String) e.getValue());
				
				abbrClassTable.put(key, value);
				classAbbrTable.putIfAbsent(value, key);
			}
			
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
			handler = this.loadInstance(SpellHandler.class, config, HANDLER_CLASS, MpSpellHandler.class, HANDLER_CONFIG, null);
			getLogger().info("Sucessfully installed spell handler.");
			
			// Spell
			registry = new SpellRegistry(this);
			this.loadConfig(registry, config, SPELL_CONFIG, new RegistryInitializer());
			getLogger().info("Successfully loaded " + registry.spellRegistries.size() + " spells.");
			
			// Buff
			buff = this.loadInstance(BuffManager.class, config, BUFFMANAGER_CLASS, 
					ScoreboardBuffManager.class, BUFFMANAGER_CONFIG, null);
			getLogger().info("Successfully loaded buff manager.");
			
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
		if(classValue == null) parent.set(classEntry, classValue = lookForName(defaultClazz));
		
		Class<T> moduleClazz = (Class<T>) lookForClass(classValue);
		T instance = moduleClazz.newInstance();
		
		this.loadConfig(instance, parent, configEntry, abscence);
		return instance;
	}
	
	public <T extends Module> void saveConfig(T instance, ConfigurationSection config, 
			String configEntry) throws Exception {
		if(!config.contains(configEntry)) config.createSection(configEntry);
		ConfigurationSection configSection = config.getConfigurationSection(configEntry);
		instance.save(this, configSection);
	}
	
	public <T extends Module> void saveInstance(T instance, ConfigurationSection config, 
			String classEntry, String configEntry) throws Exception {
		config.set(classEntry, lookForName(instance.getClass()));
		
		this.saveConfig(instance, config, configEntry);
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
			instance.save(this, config);
		}
		instance.after(this);
		return instance;
	}
	
	private void configurable(Object instance, ConfigurationSection current, boolean load) throws Exception {
		Field[] fields = instance.getClass().getFields();
		for(Field field : fields) {
			Configurable config = field.getAnnotation(Configurable.class);
			if(config != null) {
				String fieldName = config.name().length() > 0? config.name() : field.getName();
				if(load) config.value().load(fieldName, field, instance, this, current);
				else config.value().save(fieldName, field, instance, this, current);
			}
		}
	}
	
	public void loadConfigurable(Object instance, ConfigurationSection current) throws Exception {
		configurable(instance, current, true);
	}
	
	public void saveConfigurable(Object instance, ConfigurationSection current) throws Exception {
		configurable(instance, current, false);
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
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
		if(command.getName().equalsIgnoreCase("magick")) {
			sender.sendMessage("");	// A new line
			if(arguments.length == 0) {
				sender.sendMessage(ChatColor.BOLD + "Usage" + ChatColor.RESET + ": /" + label + " <subcommand> [<parameters>]");
				sender.sendMessage(ChatColor.BOLD + "Listing " + ChatColor.RESET + ChatColor.YELLOW + "subcommands" + ChatColor.RESET + ": ");
				for(String commandName : commands.keySet()) {
					CommandHandle service = commands.get(commandName);
					if(service.visible(sender))
						sender.sendMessage("  " + ChatColor.YELLOW + commandName + ChatColor.RESET + ": " + 
								service.description());
				}
				sender.sendMessage("See you on " + ChatColor.BOLD + "GitHub" + ChatColor.RESET + ": " + ChatColor.RED 
						+ "http://github.com/aegistudio/MagickElement");
			}
			else {
				CommandHandle commandTarget = commands.get(arguments[0]);
				if(commandTarget == null) return false;
				if(!commandTarget.visible(sender)) return false;
				String[] innerArguments = new String[arguments.length - 1];
				System.arraycopy(arguments, 1, innerArguments, 0, arguments.length - 1);
				commandTarget.handle(this, sender, innerArguments);
			}
			return true;
		}
		else return false;
	}
}
