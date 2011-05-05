package me.taylorkelly.mywarp;

import com.nijikokun.bukkit.Permissions.Permissions;
import org.anjocaido.groupmanager.GroupManager;
 
import me.taylorkelly.mywarp.WarpLogger;
  
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WarpPermissions {

    private enum PermissionHandler {

        PERMISSIONS, GROUP_MANAGER, NONE
    }
    private static PermissionHandler handler;
    private static Plugin permissionPlugin;

    public static void initialize(Server server) {
        Plugin groupManager = server.getPluginManager().getPlugin("GroupManager");
        Plugin permissions = server.getPluginManager().getPlugin("Permissions");

        if (groupManager != null) {
            permissionPlugin = groupManager;
            handler = PermissionHandler.GROUP_MANAGER;
            String version = groupManager.getDescription().getVersion();
            WarpLogger.info("Permissions enabled using: GroupManager v" + version);
        } else if (permissions != null) {
            permissionPlugin = permissions;
            handler = PermissionHandler.PERMISSIONS;
            String version = permissions.getDescription().getVersion();
            WarpLogger.info("Permissions enabled using: Permissions v" + version);
        } else {
            handler = PermissionHandler.NONE;
            WarpLogger.warning("A permission plugin isn't loaded.");
        }
    }

    public static boolean permission(Player player, String permission, boolean defaultPerm) {
        switch (handler) {
            case PERMISSIONS:
                return ((Permissions) permissionPlugin).getHandler().has(player, permission);
            case GROUP_MANAGER:
                return ((GroupManager) permissionPlugin).getWorldsHolder().getWorldPermissions(player).has(player, permission);
            case NONE:
                return defaultPerm;
            default:
                return defaultPerm;
        }
    }


    public static boolean isAdmin(Player player) {
        return permission(player, "mywarp.admin", player.isOp());
    }

    public static boolean warp(Player player) {
            return permission(player, "mywarp.warp.basic.warp", true);
    }

    public static boolean delete(Player player) {
            return permission(player, "mywarp.warp.basic.delete", true);
    }

    public static boolean list(Player player) {
            return permission(player, "mywarp.warp.basic.list", true);
    }

    public static boolean welcome(Player player) {
            return permission(player, "mywarp.warp.basic.welcome", true);
    }

    public static boolean search(Player player) {
            return permission(player, "mywarp.warp.basic.search", true);
    }

    public static boolean give(Player player) {
            return permission(player, "mywarp.warp.soc.give", true);
    }

    public static boolean invite(Player player) {
            return permission(player, "mywarp.warp.soc.invite", true);
    }

    public static boolean uninvite(Player player) {
            return permission(player, "mywarp.warp.soc.uninvite", true);
    }

    public static boolean canPublic(Player player) {
            return permission(player, "mywarp.warp.soc.public", true);
    }

    public static boolean canPrivate(Player player) {
            return permission(player, "mywarp.warp.soc.private", true);
    }

    public static boolean signWarp(Player player) {
            return permission(player, "mywarp.warp.sign.warp", true);
    }

    public static boolean privateCreate(Player player) {
            return permission(player, "mywarp.warp.basic.createprivate", true);
    }
    
    public static boolean publicCreate(Player player) {
            return permission(player, "mywarp.warp.basic.createpublic", true);
    }
    
    public static boolean compass(Player player) {
            return permission(player, "mywarp.warp.basic.compass", true);
    }
	
	public static boolean update(Player player) {
            return permission(player, "mywarp.warp.basic.update", true);
    }

    public static int maxPrivateWarps(Player player) {
        return WarpSettings.maxPrivate;
    }
    
    public static int maxPublicWarps(Player player) {
        return WarpSettings.maxPublic;
    }

    static boolean createSignWarp(Player player) {
            return permission(player, "mywarp.warp.sign.create", true);
    }
}

