package net.aegistudio.magick.chant;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.particle.MagickParticle;

public class ChantRecordEntry {
	public int chantTotal;
	public int chantStatus;
	public ItemStack magickBook;
	
	public ChantRecordEntry(MagickElement element, ItemStack magickBook) {
		this.magickBook = magickBook;
		chantTotal = chantStatus = 0;
		for(String spell : element.book.extractSpell(magickBook)) 
			chantTotal += (int)(element.registry.getSpell(spell).handlerInfo);
	}
	
	public void execute(Player player, MagickElement element) {
		for(String spell : element.book.extractSpell(magickBook)) {
			element.registry.getSpell(spell).makeMagick(element, player, 
					player.getLocation(), this.magickBook, spell);
		}
	}
	
	public String tips(String format) {
		return format.replace("$magick", ((BookMeta)magickBook.getItemMeta()).getTitle());
	}
	
	public MagickParticle chantParticle = new MagickParticle(Sound.LEVEL_UP) {
		protected int tier() {
			return chantStatus;
		}
		
		protected int max() {
			return chantTotal;
		}
	};
}
