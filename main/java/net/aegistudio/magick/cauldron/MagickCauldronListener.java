package net.aegistudio.magick.cauldron;

import java.util.TreeSet;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.compat.CompatibleSound;
import net.aegistudio.magick.inventory.BlockCoordinate;
import net.aegistudio.magick.particle.BlockParticle;
import net.aegistudio.magick.particle.MagickParticle;

public class MagickCauldronListener implements Listener {
	private final TreeSet<BlockCoordinate> activatedCauldrons = new TreeSet<BlockCoordinate>();
	
	private final MagickElement element;
	private final CauldronInventoryHandler inventory;
	private final String beginForging, stillForging;
	
	private final int tickInterval, tickBrewing;
	public MagickCauldronListener(MagickElement element, CauldronInventoryHandler inventory, 
			String beginForging, String stillForging, 
			int tickInterval, int tickBrewing) {
		this.element = element;
		this.beginForging = beginForging;
		this.inventory = inventory;
		this.stillForging = stillForging;
		this.tickBrewing = tickBrewing;
		this.tickInterval = tickInterval;
		
		this.glyph = new MagickParticle(CompatibleSound.ORB_PICKUP.get(element)) {
			public int tier() {
				return 10;
			}
			
			public float volume() {
				return 0.07f;
			}
			
			public float pitch() {
				return 0.5f * TONAL_PITCH_LOOKUP[(int)Math.min(Math
						.round(8.0f * Math.random()), 7)];
			}
		};
	}
	
	@EventHandler
	public void onInteractCauldron(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(event.getPlayer().isSneaking()) return;
		Block block = event.getClickedBlock();
		if(block == null) return;
		if(block.getType() != Material.CAULDRON) return;
		
		if(activatedCauldrons.contains(new BlockCoordinate(block.getLocation())))
			event.getPlayer().sendMessage(stillForging);
		else this.inventory.openInventory(event);
	}
	
	@EventHandler
	public void onBreakCauldron(BlockBreakEvent event) {
		Block block = event.getBlock();
		if(block.getType() != Material.CAULDRON) return;
		
		BlockCoordinate coord = new BlockCoordinate(block.getLocation());
		activatedCauldrons.remove(coord);
		this.inventory.breakBlock(event);
	}
	
	private final BlockParticle blockParticle = new BlockParticle(Effect.valueOf("HAPPY_VILLAGER"), 6);
	
	private final MagickParticle glyph;
	
	@EventHandler
	public void onActivateCauldron(BlockIgniteEvent event) {
		Location location = event.getBlock().getLocation().add(0, 1, 0);
		Block block = event.getBlock().getWorld().getBlockAt(location);
		if(block.getType() != Material.CAULDRON) return;
	
		BlockCoordinate coordinate = new BlockCoordinate(block.getLocation());
		if(activatedCauldrons.contains(coordinate)) return;
		
		activatedCauldrons.add(coordinate);
		new CauldronTickHandler(element, block, tickInterval, tickBrewing) {
			@Override
			protected void doTick() {
				// Validate block changes.
				if(block.getWorld().getBlockAt(block.getLocation()).getType() != Material.CAULDRON) return;
				//for(int i = 0; i < 10; i ++)
				//	block.getWorld().playEffect(block.getLocation().add(0.5, 1, 0.5), Effect.FLYING_GLYPH, null);
				glyph.play(block.getLocation().add(0.5, 1, 0.5));
			}

			@Override
			protected void endTick() {
				//Validate block changes.
				if(block.getWorld().getBlockAt(block.getLocation()).getType() != Material.CAULDRON) return;
				
				activatedCauldrons.remove(new BlockCoordinate(block.getLocation()));
				blockParticle.show(block);
				inventory.brewBlock(block);
				block.getWorld().playSound(block.getLocation(), CompatibleSound.LEVEL_UP.get(element), 1.0f, 1.0f);
			}
		};
		if(event.getPlayer() != null) 
			event.getPlayer().sendMessage(beginForging);
	}
}
