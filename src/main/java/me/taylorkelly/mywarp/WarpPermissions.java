package me.taylorkelly.mywarp;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.anjocaido.groupmanager.GroupManager;

import me.taylorkelly.mywarp.WarpLogger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WarpPermissions {

    private enum PermissionHandler {
        PERMISSIONSEX, PERMISSIONS, PERMISSIONS3, GROUPMANAGER, NONE
    }
    private static PermissionHandler handler;
    private static Plugin permissionPlugin;
    private static final WarpLogger logger = WarpLogger.getLogger();

    public static void initialize(Server server) {
    	Plugin permissionsEx = server.getPluginManager().getPlugin("PermissionsEx");
    	Plugin groupManager = server.getPluginManager().getPlugin("GroupManager");
        Plugin permissions = server.getPluginManager().getPlugin("Permissions");

        if (permissionsEx != null) {
            permissionPlugin = permissionsEx;
            handler = PermissionHandler.PERMISSIONSEX;
            String version = permissionsEx.getDescription().getVersion();
            logger.info("Permissions enabled using: PermissionsEx v" + version);
        } else if (groupManager != null) {
            permissionPlugin = groupManager;
            handler = PermissionHandler.GROUPMANAGER;
            String version = groupManager.getDescription().getVersion();
            logger.info("Permissions enabled using: GroupManager v" + version);
        } else if (permissions != null) {
            permissionPlugin = permissions;
            String version = permissions.getDescription().getVersion();
            if (version.contains("3.")) {
            	handler = PermissionHandler.PERMISSIONS3;
            } else {
            	handler = PermissionHandler.PERMISSIONS;
            }
            logger.info("Permissions enabled using: Permissions v" + version);
        } else {
            handler = PermissionHandler.NONE;
            logger.warning("A permission plugin isn't loaded.");
        }
    }

    public static boolean permission(Player player, String permission, boolean defaultPerm) {
        switch (handler) {
            case PERMISSIONSEX:
                return PermissionsEx.getPermissionManager().has(player, permission);
            case PERMISSIONS3:
            	return ((Permissions) permissionPlugin).getHandler().has(player, permission);
            case PERMISSIONS:
                return ((Permissions) permissionPlugin).getHandler().has(player, permission);
            case GROUPMANAGER:
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
