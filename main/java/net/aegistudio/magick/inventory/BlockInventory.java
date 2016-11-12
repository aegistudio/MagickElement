package net.aegistudio.magick.inventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class BlockInventory {
	private final Server server;
	public BlockInventory(Server server) {
		this.server = server;
	}
	
	private Map<BlockCoordinate, Inventory> inventories = new TreeMap<BlockCoordinate, Inventory>();
	
	public void read(File inventory) throws IOException, ClassNotFoundException {
		if(!inventory.exists()) return;
		
		ObjectInputStream inputStream = 
				new ObjectInputStream(new FileInputStream(inventory));
		
		int worldsLength = inputStream.readByte();
		String[] worlds = new String[worldsLength];
		for(int i = 0; i < worldsLength; i ++)
			worlds[i] = inputStream.readUTF();
		
		int entries = inputStream.readInt();
		for(int i = 0; i < entries; i ++) {
			World world = server.getWorld(worlds[inputStream.readByte()]);
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			BlockCoordinate blockCoord = new BlockCoordinate(new Location(world, x, y, z));
			
			int arrayCapacity = inputStream.readShort();
			ItemStack[] items = new ItemStack[arrayCapacity];
			int arrayEntries = inputStream.readShort();
			for(int e = 0; e < arrayEntries; e ++) {
				int index = inputStream.readShort();
				
				EnumItemPersistence persistence = EnumItemPersistence.values()[inputStream.readByte()];
				items[index] = persistence.read(inputStream);
			}
			
			Inventory inv = newInventory();
			inv.setContents(items);
			inventories.put(blockCoord, inv);
		}
		
		inputStream.close();
	}
	
	public void write(File inventory) throws IOException {
		if(!inventory.exists()) inventory.createNewFile();
		ObjectOutputStream outputStream =
				new ObjectOutputStream(new FileOutputStream(inventory));
		
		List<World> worlds = server.getWorlds();
		outputStream.writeByte(worlds.size());
		for(World w : worlds) outputStream.writeUTF(w.getName());
		
		int entries = inventories.size();
		outputStream.writeInt(entries);
		for(Entry<BlockCoordinate, Inventory> entry : inventories.entrySet()) {
			BlockCoordinate blockCoord = entry.getKey();
			outputStream.writeByte(worlds.indexOf(blockCoord.w));
			outputStream.writeInt(blockCoord.x);
			outputStream.writeInt(blockCoord.y);
			outputStream.writeInt(blockCoord.z);
			
			Inventory inv = entry.getValue();
			ItemStack[] items = inv.getContents();
			outputStream.writeShort(items.length);
			
			int arrayEntries = 0;
			for(ItemStack itm : items) if(itm != null)
				arrayEntries ++;
			outputStream.writeShort(arrayEntries);
			for(int e = 0; e < items.length; e ++) {
				ItemStack itm = items[e];
				if(itm == null) continue;
				outputStream.writeShort(e);
				EnumItemPersistence persistence 
					= EnumItemPersistence.judgePersistence(itm);
				
				outputStream.writeByte(persistence.ordinal());
				persistence.write(outputStream, itm);
			}
		}
		
		outputStream.close();
	}
	
	public Inventory getInventory(Block block) {
		BlockCoordinate coord = new BlockCoordinate(block.getLocation());
		Inventory inventory = inventories.get(coord);
		if(inventory == null) inventories.put(coord, inventory = newInventory());
		return inventory;
	}
	
	public void blockBreak(Block block) {
		BlockCoordinate coord = new BlockCoordinate(block.getLocation());
		Inventory inventory = inventories.remove(coord);
		if(inventory == null) return;
		for(ItemStack item : inventory.getContents())
			if(item != null) block.getWorld().dropItem(block.getLocation(), item);
	}
	
	protected abstract Inventory newInventory();
}
