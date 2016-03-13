package me.taylorkelly.mywarp.permissions;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;

public class GroupManagerHandler implements IPermissionsHandler {
	private final transient GroupManager manager;

	public GroupManagerHandler(final Plugin permissionsPlugin) {
		manager = ((GroupManager)permissionsPlugin);
	}

	@Override
	public boolean hasPermission(final Player player, final String node, final boolean defaultPerm) {
		AnjoPermissionsHandler handler = manager.getWorldsHolder().getWorldPermissions(player);
		return handler.has(player, node);
	}

	@Override
	public int getInteger(final Player player, final String node, final int defaultInt) {
		String playername = player.getName();
		AnjoPermissionsHandler handler = manager.getWorldsHolder().getWorldPermissions(player);
		return handler.getPermissionInteger(playername, node);
	}
}
