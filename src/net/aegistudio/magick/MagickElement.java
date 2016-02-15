package net.aegistudio.magick;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.aegistudio.magick.book.BookFactory;
import net.aegistudio.magick.book.MagickBook;
import net.aegistudio.magick.book.PagingBookFactory;
import net.aegistudio.magick.cauldron.CauldronInventoryHandler;
import net.aegistudio.magick.cauldron.CauldronFactory;
import net.aegistudio.magick.cauldron.MagickCauldronFactory;
import net.aegistudio.magick.element.ElementDefinition;
import net.aegistudio.magick.element.ElementHolder;
import net.aegistudio.magick.element.ItemDamagePair;
import net.aegistudio.magick.mp.MpSpellHandler;
import net.aegistudio.magick.spell.SpellEntry;
import net.aegistudio.magick.spell.SpellHandler;
import net.aegistudio.magick.spell.SpellRegistry;
import net.aegistudio.magick.spell.SpellStub;

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
			String bookFactoryClazz = config.getString(BOOK_FACTORY);
			if(bookFactoryClazz == null) config.set(BOOK_FACTORY, 
					bookFactoryClazz = PagingBookFactory.class.getName());
			
			BookFactory bookListenerFactory = 
					((Class<? extends BookFactory>) Class.forName(bookFactoryClazz)).newInstance();
			getLogger().info("Sucessfully found book factory: " + bookFactoryClazz);
			
			ConfigurationSection bookListenerConfig = config.getConfigurationSection(BOOK_CONFIG);
			if(bookListenerConfig == null) bookListenerConfig = config.createSection(BOOK_CONFIG);
			
			bookListenerFactory.setConfig(bookListenerConfig);
			getLogger().info("Sucessfully loaded book configuration.");

			this.book = bookListenerFactory.newMagickBook(this);
			getLogger().info("Sucessfully installed book.");
			
			super.getServer().getPluginManager().registerEvents(bookListenerFactory.newBookListener(this), this);
			getLogger().info("Sucessfully installed book listener.");
			
			// Cauldron
			String cauldronFactoryClazz = config.getString(CAULDRON_FACTORY);
			if(cauldronFactoryClazz == null) config.set(CAULDRON_FACTORY, 
					cauldronFactoryClazz = MagickCauldronFactory.class.getName());
			CauldronFactory cauldronListenerFactory = 
					((Class<? extends CauldronFactory>) Class.forName(cauldronFactoryClazz)).newInstance();
			getLogger().info("Sucessfully found cauldron factory: " + cauldronFactoryClazz);
			
			ConfigurationSection cauldronListenerConfig = config.getConfigurationSection(CAULDRON_CONFIG);
			if(cauldronListenerConfig == null) cauldronListenerConfig = config.createSection(CAULDRON_CONFIG);
			
			cauldronListenerFactory.loadConfig(cauldronListenerConfig);
			getLogger().info("Sucessfully loaded cauldron configuration.");
			
			cauldron = cauldronListenerFactory.newInventoryHanlder(this);
			cauldron.loadData(getDataFolder());
			getLogger().info("Sucessfully loaded cauldrons inventories.");
			
			super.getServer().getPluginManager().registerEvents(cauldronListenerFactory.newCauldronListener(this), this);
			getLogger().info("Sucessfully installed cauldron listener.");
			
			// Element
			element = new ElementHolder();
			ConfigurationSection elementSection;
			if(config.contains(ELEMENT_CONFIG)) 
				element.load(elementSection = config.getConfigurationSection(ELEMENT_CONFIG));
			else {
				elementSection = config.createSection(ELEMENT_CONFIG);
				
				ItemDamagePair blazeRod = new ItemDamagePair(Material.BLAZE_ROD, -1);
				ElementDefinition blazeRodDefinition = new ElementDefinition();
				blazeRodDefinition.setElementPoint("fire", 3);
				blazeRodDefinition.setElementPoint("divine", 1);
				blazeRodDefinition.setElementPoint("evil", 1);
				element.element.put(blazeRod, blazeRodDefinition);
			}
			element.save(elementSection);
			
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
			ConfigurationSection spellConfig;
			if(config.contains(SPELL_CONFIG))
				registry.loadConfig(spellConfig = config.getConfigurationSection(SPELL_CONFIG));
			else {
				spellConfig = config.createSection(SPELL_CONFIG);
				
				SpellEntry stub = new SpellEntry(this);
				stub.effect = new SpellStub();
				stub.spellPrice = new ElementDefinition();
				stub.spellPrice.setElementPoint("fire", 1);
				stub.handlerInfo = 1;
				
				registry.spellRegistries.put("stub", stub);
			}
			registry.saveConfig(spellConfig);
			
			super.saveConfig();
		}
		catch(Exception e) {
			getLogger().severe("Magick Element failed to load because of exception.");
			throw new RuntimeException(e);
		}
	}
	
	public void onDisable() {
		try {
			cauldron.saveData(super.getDataFolder());
			getLogger().info("Sucessfully saved cauldron inventories.");
		}
		catch(Exception e) {
			getLogger().severe("Magick Element failed to save because of exception.");
			throw new RuntimeException(e);
		}
	}
}
