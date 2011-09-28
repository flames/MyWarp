package me.taylorkelly.mywarp;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MWPlayerListener extends PlayerListener
{
  private WarpList warpList;

  public MWPlayerListener(WarpList warpList)
  {
    this.warpList = warpList;
  }

  public void onPlayerInteract(PlayerInteractEvent event)
  {
    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      Block block = event.getClickedBlock();
      if (((block.getState() instanceof Sign)) && (SignWarp.isSignWarp((Sign)block.getState())) && (WarpPermissions.signWarp(event.getPlayer())))
        SignWarp.warpSign((Sign)block.getState(), this.warpList, event.getPlayer());
    }
  }

  public void onPlayerChat(PlayerChatEvent event)
  {
    Player player = event.getPlayer();
    if (this.warpList.waitingForWelcome(player)) {
      this.warpList.setWelcomeMessage(player, event.getMessage());
      this.warpList.notWaiting(player);
      event.setCancelled(true);
    }
  }
  
  @Override
  public void onPlayerTeleport(PlayerTeleportEvent event) {
	World world = event.getPlayer().getWorld();
	Chunk chunk = world.getChunkAt(event.getTo());
	int x = chunk.getX();
	int z = chunk.getZ();
	world.refreshChunk(x, z);
  }
}