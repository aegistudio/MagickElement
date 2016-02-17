package net.aegistudio.magick;

import org.bukkit.Material;

import net.aegistudio.magick.element.ElementDefinition;
import net.aegistudio.magick.element.ElementHolder;
import net.aegistudio.magick.element.ItemDamagePair;

public class ElementInitializer implements Initializer<ElementHolder> {

	@Override
	public void initial(MagickElement magick, ElementHolder element) {
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

}
