package net.aegistudio.magick.cauldron;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.element.ElementDefinition;
import net.aegistudio.magick.element.ItemDamagePair;
import net.aegistudio.magick.inventory.BlockInventory;
import net.aegistudio.magick.spell.SpellEntry;

public class CauldronInventoryHandler {
	private final BlockInventory inventory;
	private final MagickElement element;
	public CauldronInventoryHandler(MagickElement magickElement, int cauldronCapacity, String cauldronTitle) {
		this.element = magickElement;
		this.inventory = new BlockInventory(magickElement.getServer()) {
			@Override
			protected Inventory newInventory() {
				return element.getServer().createInventory(null, cauldronCapacity * 9, cauldronTitle);
			}
		};
	}
	
	public void openInventory(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		Inventory inv =  inventory.getInventory(block);
		event.getPlayer().openInventory(inv);
		event.setCancelled(true);		
	}
	
	public void breakBlock(BlockBreakEvent event) {
		Block block = event.getBlock();
		inventory.blockBreak(block);
	}
	
	public void brewBlock(Block block) {
		Inventory content = inventory.getInventory(block);
		ElementDefinition grossCost = new ElementDefinition();
		
		for(int pointer = 0; pointer < content.getSize(); pointer ++) {
			ItemStack item = content.getItem(pointer);
			if(item == null) continue;
			if(item.getType() != Material.WRITTEN_BOOK) continue;
			if(element.book.isMagickBook(item)) continue;
			
			BookMeta book = (BookMeta) item.getItemMeta();
			ElementDefinition cost = new ElementDefinition();
			cost.accum(grossCost, 1);
			
			ArrayList<String> validSpell = new ArrayList<String>();
			int[] requiredItem = new int[content.getSize()];
			
			for(String spell : book.getPages()) {
				// Expel invalid spells from book content.
				SpellEntry spellEntry = element.registry.getSpell(spell);
				if(spellEntry == null) continue;

				// Make calcuation for this book.
				cost.accum(spellEntry.spellPrice, item.getAmount());
				for(int i = 0; i < content.getSize(); i ++) {
					// If enough spell cost is collected, end this progress.
					if(cost.redundant()) break;
					
					ItemStack itemStack = content.getItem(i);
					
					// Do not adsorb any book.
					if(itemStack == null) continue;
					if(itemStack.getType() == Material.WRITTEN_BOOK) continue;
					
					// Reduce price.
					ElementDefinition define = element.element.definition(itemStack);
					while(requiredItem[i] < itemStack.getAmount()
							&& cost.helpful(define)) {
						
							cost.accum(define, -1);
							requiredItem[i] ++;
					}
				}
				if(cost.redundant())
					validSpell.add(spell);
				else break;
			}
			
			if(cost.redundant()) {
				book.setPages(validSpell);
				item.setItemMeta(book);
				element.book.makeMagickBook(item);
				ArrayList<ItemStack> transformedList = new ArrayList<ItemStack>();
				for(int i = 0; i < requiredItem.length; i ++) {
					if(requiredItem[i] == 0) continue;
					else {
						ItemStack target = content.getItem(i);
						if(requiredItem[i] == target.getAmount()) 
							content.removeItem(target);
						else 
							target.setAmount(target.getAmount() - requiredItem[i]);
						
						ItemDamagePair transformed = element.element.transform(target);
						if(transformed != null) {
							if(transformed.damage == -1) 
								transformedList.add(new ItemStack(transformed.material, requiredItem[i], target.getDurability()));
							else
								transformedList.add(new ItemStack(transformed.material, requiredItem[i], (short) transformed.damage));
						}
					}
				}
				content.addItem(transformedList.toArray(new ItemStack[0]));
				grossCost = cost;
			}
		}
	}
	
	public static final String INVENTORY_FILE = "cauldrons.inv";
	public void loadData(File dataFolder) throws ClassNotFoundException, IOException {
		this.inventory.read(new File(dataFolder, INVENTORY_FILE));
	}
	
	public void saveData(File dataFolder) throws IOException {
		this.inventory.write(new File(dataFolder, INVENTORY_FILE));
	}
}
