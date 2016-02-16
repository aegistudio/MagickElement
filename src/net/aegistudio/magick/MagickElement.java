package net.aegistudio.magick;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import net.aegistudio.magick.book.BookFactory;
import net.aegistudio.magick.book.MagickBook;
import net.aegistudio.magick.book.PagingBookFactory;
import net.aegistudio.magick.cauldron.CauldronInventoryHandler;
import net.aegistudio.magick.cauldron.CauldronFactory;
import net.aegistudio.magick.cauldron.MagickCauldronFactory;
import net.aegistudio.magick.effect.EntityRain;
import net.aegistudio.magick.effect.FeatherFall;
import net.aegistudio.magick.element.ElementDefinition;
import net.aegistudio.magick.element.ElementHolder;
import net.aegistudio.magick.element.ItemDamagePair;
import net.aegistudio.magick.mp.MpSpellHandler;
import net.aegistudio.magick.spell.SpellEntry;
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
				
				{
					ItemDamagePair blazeRod = new ItemDamagePair(Material.BLAZE_ROD, -1);
					ElementDefinition blazeRodDefinition = new ElementDefinition();
					blazeRodDefinition.setElementPoint("fire", 3);
					blazeRodDefinition.setElementPoint("divine", 1);
					blazeRodDefinition.setElementPoint("evil", 1);
					element.element.put(blazeRod, blazeRodDefinition);
				}
				
				{
					ItemDamagePair blazePowder = new ItemDamagePair(Material.BLAZE_POWDER, -1);
					ElementDefinition blazePowderDefinition = new ElementDefinition();
					blazePowderDefinition.setElementPoint("fire", 2);
					element.element.put(blazePowder, blazePowderDefinition);
				}
				
				{
					ItemDamagePair feather = new ItemDamagePair(Material.FEATHER, -1);
					ElementDefinition featherDefinition = new ElementDefinition();
					featherDefinition.setElementPoint("wind", 2);
					featherDefinition.setElementPoint("divine", 1);
					element.element.put(feather, featherDefinition);
				}
				
				{
					ItemDamagePair redstonePowder = new ItemDamagePair(Material.REDSTONE, -1);
					ElementDefinition redstonePowderDefinition = new ElementDefinition();
					redstonePowderDefinition.setElementPoint("electric", 1);
					element.element.put(redstonePowder, redstonePowderDefinition);
				}
				
				{
					ItemDamagePair redstoneBlock = new ItemDamagePair(Material.REDSTONE_BLOCK, -1);
					ElementDefinition redstoneBlockDefinition = new ElementDefinition();
					redstoneBlockDefinition.setElementPoint("electric", 9);
					element.element.put(redstoneBlock, redstoneBlockDefinition);
				}
				
				{
					ItemDamagePair waterBucket = new ItemDamagePair(Material.WATER_BUCKET, -1);
					ElementDefinition waterBucketDefinition = new ElementDefinition();
					waterBucketDefinition.setElementPoint("water", 2);
					element.element.put(waterBucket, waterBucketDefinition);					
					element.transform.put(waterBucket, new ItemDamagePair(Material.BUCKET, -1));
				}
				
				{
					ItemDamagePair lavaBucket = new ItemDamagePair(Material.LAVA_BUCKET, -1);
					ElementDefinition lavaBucketDefinition = new ElementDefinition();
					lavaBucketDefinition.setElementPoint("fire", 2);
					element.element.put(lavaBucket, lavaBucketDefinition);					
					element.transform.put(lavaBucket, new ItemDamagePair(Material.BUCKET, -1));
				}
				
				{
					ItemDamagePair waterPotion = new ItemDamagePair(Material.POTION, 0);
					ElementDefinition waterPotionDefinition = new ElementDefinition();
					waterPotionDefinition.setElementPoint("water", 1);
					element.element.put(waterPotion, waterPotionDefinition);
					element.transform.put(waterPotion, new ItemDamagePair(Material.GLASS_BOTTLE, -1));
				}
				
				{
					ItemDamagePair ice = new ItemDamagePair(Material.ICE, -1);
					ElementDefinition iceDefinition = new ElementDefinition();
					iceDefinition.setElementPoint("ice", 4);
					iceDefinition.setElementPoint("water", 1);
					element.element.put(ice, iceDefinition);
				}
				
				{
					ItemDamagePair snowblock = new ItemDamagePair(Material.SNOW_BLOCK, -1);
					ElementDefinition snowBlockDefinition = new ElementDefinition();
					snowBlockDefinition.setElementPoint("ice", 1);
					snowBlockDefinition.setElementPoint("divine", 1);
					element.element.put(snowblock, snowBlockDefinition);
				}
				
				{
					ItemDamagePair ghastTear = new ItemDamagePair(Material.GHAST_TEAR, -1);
					ElementDefinition ghastTearDefinition = new ElementDefinition();
					ghastTearDefinition.setElementPoint("divine", 4);
					ghastTearDefinition.setElementPoint("evil", 1);
					element.element.put(ghastTear, ghastTearDefinition);
				}
				
				{
					ItemDamagePair magmaCream = new ItemDamagePair(Material.MAGMA_CREAM, -1);
					ElementDefinition magmaCreamDefinition = new ElementDefinition();
					magmaCreamDefinition.setElementPoint("fire", 2);
					magmaCreamDefinition.setElementPoint("evil", 1);
					magmaCreamDefinition.setElementPoint("sticky", 1);
					element.element.put(magmaCream, magmaCreamDefinition);
				}
				
				{
					ItemDamagePair slimeBall = new ItemDamagePair(Material.SLIME_BALL, -1);
					ElementDefinition slimeBallDefinition = new ElementDefinition();
					slimeBallDefinition.setElementPoint("sticky", 1);
					element.element.put(slimeBall, slimeBallDefinition);
				}
				
				{
					ItemDamagePair string = new ItemDamagePair(Material.STRING, -1);
					ElementDefinition stringDefinition = new ElementDefinition();
					stringDefinition.setElementPoint("sticky", 1);
					element.element.put(string, stringDefinition);
				}
				
				{
					ItemDamagePair web = new ItemDamagePair(Material.WEB, -1);
					ElementDefinition webDefinition = new ElementDefinition();
					webDefinition.setElementPoint("sticky", 6);
					webDefinition.setElementPoint("evil", 1);
					element.element.put(web, webDefinition);
				}
				
				{
					ItemDamagePair dragonEgg = new ItemDamagePair(Material.DRAGON_EGG, -1);
					ElementDefinition dragonEggDefinition = new ElementDefinition();
					dragonEggDefinition.setElementPoint("evil", 60);
					dragonEggDefinition.setElementPoint("divine", 30);
					element.element.put(dragonEgg, dragonEggDefinition);
				}
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
				registry.loadConfig(this, spellConfig = config.getConfigurationSection(SPELL_CONFIG));
			else {
				spellConfig = config.createSection(SPELL_CONFIG);
				{
					SpellEntry meteorite = new SpellEntry(this);
					EntityRain meteoriteRain = new EntityRain();
					meteorite.effect = meteoriteRain;
					meteoriteRain.cluster = 4;
					meteoriteRain.tier = 8;
					meteoriteRain.delay = 20;
					meteoriteRain.entity = EntityType.FIREBALL;
					
					meteorite.spellPrice = new ElementDefinition();
					meteorite.spellPrice.setElementPoint("fire", 20);
					meteorite.spellPrice.setElementPoint("divine", 10);
					meteorite.spellPrice.setElementPoint("evil", 10);
					
					meteorite.handlerInfo = 30;
					registry.spellRegistries.put("meteorite", meteorite);
				}
				
				{
					SpellEntry featherFall = new SpellEntry(this);
					FeatherFall featherFalling = new FeatherFall();
					featherFall.effect = featherFalling;
					
					featherFall.spellPrice = new ElementDefinition();
					featherFall.spellPrice.setElementPoint("wind", 80);
					
					featherFall.handlerInfo = 20;
					registry.spellRegistries.put("featherFall", featherFall);
				}
			}
			registry.saveConfig(this, spellConfig);
			
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
