package me.taylorkelly.mywarp.listeners;

import me.taylorkelly.mywarp.MyWarp;
import me.taylorkelly.mywarp.data.SignWarp;
import me.taylorkelly.mywarp.permissions.WarpPermissions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class MWBlockListener implements Listener
{
  private WarpPermissions warpPermissions;

  public MWBlockListener(MyWarp plugin)
  {
      warpPermissions = MyWarp.getWarpPermissions();
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onSignChange(SignChangeEvent event)
  {
    Player player = event.getPlayer();

    if (SignWarp.isSignWarp(event))
      if (warpPermissions.createSignWarp(player)) {
        player.sendMessage(ChatColor.AQUA + "Successfully created a SignWarp");
      }
      else {
        player.sendMessage(ChatColor.RED + "You do not have permission to create a SignWarp");
        event.setCancelled(true);
      }
  }
}