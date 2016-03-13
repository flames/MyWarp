package me.taylorkelly.mywarp.permissions;

import org.bukkit.entity.Player;

public class NullHandler implements IPermissionsHandler {
	
	@Override
	public boolean hasPermission(final Player player, final String node, boolean defaultPerm) {
		return false;
	}
	
	public int getInteger(final Player player, final String node, int defaultInt) {
		return defaultInt;
	}
}
