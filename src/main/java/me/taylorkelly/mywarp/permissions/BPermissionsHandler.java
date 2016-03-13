package me.taylorkelly.mywarp.permissions;

import org.bukkit.entity.Player;
import de.bananaco.permissions.info.InfoReader;

public class BPermissionsHandler implements IPermissionsHandler {
	private final transient InfoReader info;
	
	public BPermissionsHandler() {
		info = new InfoReader();
		info.instantiate();
	}

	@Override
	public boolean hasPermission(final Player player, final String node, boolean defaultPerm) {
		return player.hasPermission(node);
	}
	
	public int getInteger(final Player player, final String node, int defaultInt) {
		int value = Integer.parseInt(info.getValue(player, node));
		return value;
	}
}
