package me.taylorkelly.mywarp.permissions;

import me.taylorkelly.mywarp.WarpSettings;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WarpPermissions {
	private PermissionsHandler permissionsHandler;

	public WarpPermissions(Plugin plugin) {
		permissionsHandler = new PermissionsHandler(plugin);
	}
	
	public int integer(Player player, String node, int defaultInt) {
		return permissionsHandler.getInteger(player, node, defaultInt);
	}

	public boolean hasPermission(Player player, final String node, boolean defaultPerm) {
	    return permissionsHandler.hasPermission(player, node, defaultPerm);
	}

    public boolean isAdmin(Player player) {
        return permissionsHandler.hasPermission(player, "mywarp.admin", player.isOp());
    }

    public boolean warp(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.warp", true);
    }

    public boolean delete(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.delete", true);
    }

    public boolean list(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.list", true);
    }

    public boolean welcome(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.welcome", true);
    }

    public boolean search(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.search", true);
    }

    public boolean give(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.soc.give", true);
    }

    public boolean invite(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.soc.invite", true);
    }

    public boolean uninvite(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.soc.uninvite", true);
    }

    public boolean canPublic(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.soc.public", true);
    }

    public boolean canPrivate(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.soc.private", true);
    }

    public boolean signWarp(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.sign.warp", true);
    }

    public boolean privateCreate(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.createprivate", true);
    }
    
    public boolean publicCreate(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.createpublic", true);
    }
    
    public boolean compass(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.basic.compass", true);
    }

    public int maxTotalWarps(Player player) {
        return WarpSettings.maxTotal;
    }

    public int maxPrivateWarps(Player player) {
        return WarpSettings.maxPrivate;
    }

    public int maxPublicWarps(Player player) {
        return WarpSettings.maxPublic;
    }

    public boolean createSignWarp(Player player) {
            return permissionsHandler.hasPermission(player, "mywarp.warp.sign.create", true);
    }
}