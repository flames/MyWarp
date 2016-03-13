package me.taylorkelly.mywarp.permissions;

import org.bukkit.entity.Player;
import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;

public class BPermissions2Handler implements IPermissionsHandler {
	
	public BPermissions2Handler() {

	}

	@Override
	public boolean hasPermission(final Player player, final String node, boolean defaultPerm) {
		return player.hasPermission(node);
	}
	
	public int getInteger(final Player player, final String node, int defaultInt) {
		int value = defaultInt;
		String retval = ApiLayer.getValue(player.getWorld().getName(), CalculableType.USER, player.getName(), node);
		if(retval != "" && retval != null) {
			value = Integer.parseInt(retval);
		}
		return value;
	}
}
