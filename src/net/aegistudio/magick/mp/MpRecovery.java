package net.aegistudio.magick.mp;

import org.bukkit.entity.Player;

import net.aegistudio.magick.MagickElement;

public class MpRecovery implements Runnable {
	private final MpSpellHandler mp;
	private final MagickElement element;
	public MpRecovery(MagickElement element, MpSpellHandler mp) {
		this.element = element;
		this.mp = mp;
	}
	
	@Override
	public void run() {
		Player[] players;
		if(mp.onlineOnly) players = element.getServer().getOnlinePlayers().toArray(new Player[0]);
		else players = element.getServer().getWhitelistedPlayers().toArray(new Player[0]);
		
		for(Player player : players) 
			mp.addMp(player, mp.recoveryCount);
	}
}
