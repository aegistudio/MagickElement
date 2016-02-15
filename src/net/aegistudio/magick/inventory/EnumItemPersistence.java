package net.aegistudio.magick.inventory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public enum EnumItemPersistence {
	NORMAL {
		@Override
		protected void writeItemMeta(DataOutputStream output, ItemMeta metadata) throws IOException {	}

		@Override
		protected void readItemMeta(DataInputStream input, ItemMeta metadata) throws IOException {	}
		
	}, ENCHANTED {
		@Override
		protected void writeItemMeta(DataOutputStream output, ItemMeta metadataw) throws IOException {
			this.writeEnchantments(output, metadataw.getEnchants());
			
			if(metadataw instanceof EnchantmentStorageMeta) {
				EnchantmentStorageMeta metadata = (EnchantmentStorageMeta) metadataw;
				this.writeEnchantments(output, metadata.getStoredEnchants());
			}
		}

		@SuppressWarnings("deprecation")
		private void writeEnchantments(DataOutputStream output, Map<Enchantment, Integer> enchants) throws IOException {
			output.writeInt(enchants.size());
			
			for(Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
				output.writeInt(enchant.getKey().getId());
				output.writeInt(enchant.getValue());
			}
		}
		
		@Override
		protected void readItemMeta(DataInputStream input, ItemMeta metadataw) throws IOException {
			for(Entry<Enchantment, Integer> entry : this.readEnchantments(input).entrySet())
				metadataw.addEnchant(entry.getKey(), entry.getValue(), true);
			
			if(metadataw instanceof EnchantmentStorageMeta) {
				EnchantmentStorageMeta metadata = (EnchantmentStorageMeta) metadataw;
				for(Entry<Enchantment, Integer> entry : this.readEnchantments(input).entrySet())
					metadata.addStoredEnchant(entry.getKey(), entry.getValue(), true);
			}
		}
		
		@SuppressWarnings("deprecation")
		private Map<Enchantment, Integer> readEnchantments(DataInputStream input) throws IOException {
			Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
			int enchantes = input.readInt();
			for(int i = 0; i < enchantes; i ++) {
				Enchantment enchant = Enchantment.getById(input.readInt());
				int level = input.readInt();
				enchants.put(enchant, level);
			}
			return enchants;
		}
	}, LORE {
		@Override
		protected void writeItemMeta(DataOutputStream output, ItemMeta metadata) throws IOException {
			List<String> lores = metadata.getLore();
			output.writeByte(lores.size());
			for(String lore : lores) output.writeUTF(lore);
		}

		@Override
		protected void readItemMeta(DataInputStream input, ItemMeta metadata) throws IOException {
			int loreLength = input.readByte();
			ArrayList<String> lores = new ArrayList<String>();
			for(int i = 0; i < loreLength; i ++)
				lores.add(input.readUTF());
			metadata.setLore(lores);
		}
	}, ENCHANTED_AND_LORE {
		@Override
		protected void writeItemMeta(DataOutputStream output, ItemMeta metadata) throws IOException {
			ENCHANTED.writeItemMeta(output, metadata);
			LORE.writeItemMeta(output, metadata);
		}

		@Override
		protected void readItemMeta(DataInputStream input, ItemMeta metadata) throws IOException {
			ENCHANTED.readItemMeta(input, metadata);
			LORE.readItemMeta(input, metadata);
		}		
	}, WRITTEN_BOOK {
		@Override
		protected void writeItemMeta(DataOutputStream output, ItemMeta metadata) throws IOException {
			BookMeta bookMeta = (BookMeta) metadata;
			output.writeUTF(bookMeta.getTitle());
			output.writeUTF(bookMeta.getAuthor());
			output.writeInt(bookMeta.getPageCount());
			List<String> pages = bookMeta.getPages();
			for(String page : pages) output.writeUTF(page);
		}

		@Override
		protected void readItemMeta(DataInputStream input, ItemMeta metadata) throws IOException {
			BookMeta bookMeta = (BookMeta) metadata;
			bookMeta.setTitle(input.readUTF());
			bookMeta.setAuthor(input.readUTF());
			String[] pages = new String[input.readInt()];
			for(int i = 0; i < pages.length; i ++)
				pages[i] = input.readUTF();
			bookMeta.setPages(pages);
		}
	}, BOOK_AND_ENCHANT {
		@Override
		protected void writeItemMeta(DataOutputStream output, ItemMeta metadata) throws IOException {
			ENCHANTED.writeItemMeta(output, metadata);
			WRITTEN_BOOK.writeItemMeta(output, metadata);
		}

		@Override
		protected void readItemMeta(DataInputStream input, ItemMeta metadata) throws IOException {
			ENCHANTED.readItemMeta(input, metadata);
			WRITTEN_BOOK.readItemMeta(input, metadata);
		}	
	}, BOOK_AND_LORE {
		@Override
		protected void writeItemMeta(DataOutputStream output, ItemMeta metadata) throws IOException {
			WRITTEN_BOOK.writeItemMeta(output, metadata);
			LORE.writeItemMeta(output, metadata);
		}

		@Override
		protected void readItemMeta(DataInputStream input, ItemMeta metadata) throws IOException {
			WRITTEN_BOOK.readItemMeta(input, metadata);
			LORE.readItemMeta(input, metadata);
		}	
	}, BOOK_ENCHANT_AND_LORE {
		@Override
		protected void writeItemMeta(DataOutputStream output, ItemMeta metadata) throws IOException {
			WRITTEN_BOOK.writeItemMeta(output, metadata);
			ENCHANTED.writeItemMeta(output, metadata);
			LORE.writeItemMeta(output, metadata);
		}

		@Override
		protected void readItemMeta(DataInputStream input, ItemMeta metadata) throws IOException {
			WRITTEN_BOOK.readItemMeta(input, metadata);
			ENCHANTED.readItemMeta(input, metadata);
			LORE.readItemMeta(input, metadata);
		}	
	};

	@SuppressWarnings("deprecation")
	public ItemStack read(InputStream input) throws IOException {
		DataInputStream din = new DataInputStream(input);
		int itemId = din.readInt();
		short damage = din.readShort();
		int count = din.readInt();
		ItemStack itemStack = new ItemStack(itemId, count, damage);
		ItemMeta itemMeta = itemStack.getItemMeta();
		this.readItemMeta(din, itemMeta);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	@SuppressWarnings("deprecation")
	public void write(OutputStream output, ItemStack item) throws IOException {
		DataOutputStream dout = new DataOutputStream(output);
		dout.writeInt(item.getTypeId());
		dout.writeShort(item.getDurability());
		dout.writeInt(item.getAmount());
		this.writeItemMeta(dout, item.getItemMeta());
	}
	
	protected abstract void writeItemMeta(DataOutputStream output, ItemMeta metadata) throws IOException;	
	protected abstract void readItemMeta(DataInputStream input, ItemMeta metadata) throws IOException;
	
	public static EnumItemPersistence judgePersistence(ItemStack item) {
		int ordinalBits = 0;
		if(item.getEnchantments().size() > 0) ordinalBits |= 1;
		if(item.getItemMeta() instanceof EnchantmentStorageMeta) ordinalBits |= 1;
		if(item.getItemMeta().hasLore()) ordinalBits |= 2;
		if(item.getItemMeta() instanceof BookMeta) ordinalBits |= 4;
		return values()[ordinalBits];
	}
}
