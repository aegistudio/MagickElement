package net.aegistudio.magick.mp;

import org.bukkit.entity.Player;

public class MpCooldown implements Runnable {
	private final MpSpellHandler mp;
	private final String player;
	public MpCooldown(MpSpellHandler mp, Player player) {
		mp.element.getServer().getScheduler()
			.runTaskLater(mp.element, this, mp.cooldown);
		this.mp = mp;
		this.player = player.getName();
		mp.cooling.add(this.player);
	}

	@Override
	public void run() {
		this.mp.cooling.remove(player);
	}
}
