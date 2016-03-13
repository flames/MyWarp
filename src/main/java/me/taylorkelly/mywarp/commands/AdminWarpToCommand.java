package me.taylorkelly.mywarp.commands;

import java.util.Arrays;

import me.taylorkelly.mywarp.MyWarp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminWarpToCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public AdminWarpToCommand(MyWarp plugin)
    {
        super("WarpPlayer");
        this.plugin = plugin;
        setDescription("Warp §8<player>§e to §9<name>");
        setUsage("/warp player §8<player> §9<name>");
        setArgumentRange(2, 255);
        setIdentifiers("player");
        setPermission("mywarp.admin");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            Player invitee = plugin.getServer().getPlayer(args[0]);
            // String inviteeName = (invitee == null) ? args[0] : invitee.getName();

            // TODO ChunkLoading
            plugin.getWarpList().adminWarpTo(StringUtils.join(Arrays.asList(args).subList(1, args.length), ' '), invitee, (Player) executor);
        }
        else {
            executor.sendMessage("Console cannot warp players to locations!");
        }
        return true;
    }
}
