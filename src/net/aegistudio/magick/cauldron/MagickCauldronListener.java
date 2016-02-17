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
import net.aegistudio.magick.inventory.BlockCoordinate;
import net.aegistudio.magick.particle.BlockParticle;

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
	}
	
	@EventHandler
	public void onInteractCauldron(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
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
	
	private final BlockParticle blockParticle = new BlockParticle(Effect.HAPPY_VILLAGER, 6);
	
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
				for(int i = 0; i < 10; i ++)
					block.getWorld().playEffect(block.getLocation().add(0.5, 1, 0.5), Effect.FLYING_GLYPH, null);
			}

			@Override
			protected void endTick() {
				//Validate block changes.
				if(block.getWorld().getBlockAt(block.getLocation()).getType() != Material.CAULDRON) return;
				
				activatedCauldrons.remove(new BlockCoordinate(block.getLocation()));
				blockParticle.show(block);
				inventory.brewBlock(block);
			}
		};
		if(event.getPlayer() != null) 
			event.getPlayer().sendMessage(beginForging);
	}
}
