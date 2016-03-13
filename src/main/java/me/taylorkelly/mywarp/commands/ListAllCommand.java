package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListAllCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public ListAllCommand(MyWarp plugin)
    {
        super("ListAll");
        this.plugin = plugin;
        setDescription("List the warps you can visit");
        setUsage("/warp slist");
        setArgumentRange(0, 0);
        setIdentifiers("slist");
        setPermission("mywarp.warp.basic.list");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            plugin.getWarpList().list((Player) executor);
        }
        else {
            executor.sendMessage("Console cannot list warps for themselves!");
        }

        return true;
    }
}
