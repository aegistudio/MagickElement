package net.aegistudio.magick.book;

import org.bukkit.Effect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;

import net.aegistudio.magick.MagickElement;

public class MagickBookListener implements Listener {
	private final String tryToClone;
	private final MagickElement element;
	public MagickBookListener(MagickElement element, String tryToClone) {
		this.tryToClone = tryToClone;
		this.element = element;
		
	}
	
	/** Do action when using magick book. **/
	@EventHandler
	public void onItemUse(PlayerInteractEvent event) {
		if(!this.element.book.isMagickBook(event.getItem())) return;
		
		// Magic power return to terra.
		for(int i = 0; i < 100; i ++)
			event.getPlayer().playEffect(event.getPlayer().getLocation(), Effect.FLYING_GLYPH, null);
		
		this.element.handler.handleSpell(event.getPlayer(), event.getItem());
		event.setCancelled(true);
	}
	
	/** Prevent magick book from cloning. **/
	@EventHandler
	public void onBookClone(CraftItemEvent event) {
		// Check book.
		if(event.getSlotType() != SlotType.RESULT) return;
		if(!this.element.book.isMagickBook(event.getCurrentItem())) return ;
		
		// Modifying crafting result.
		this.element.book.cloneMagickBook(event.getCurrentItem());
		event.getWhoClicked().sendMessage(tryToClone);
	}
}
