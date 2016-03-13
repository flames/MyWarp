package me.taylorkelly.mywarp.permissions;

import me.taylorkelly.mywarp.WarpSettings;

import org.bukkit.entity.Player;

public class SuperpermsHandler implements IPermissionsHandler {
	
	@Override
	public boolean hasPermission(final Player player, final String node, boolean defaultPerm) {
		if(player.isOp() && WarpSettings.opPermissions) {
			return true;
		}
		return player.hasPermission(node);
	}

	@Override
	public int getInteger(final Player player, final String node, final int defaultInt) {
		if(player.isOp() && WarpSettings.opPermissions) {
			return 0;
		}
		return defaultInt;
	}
}

