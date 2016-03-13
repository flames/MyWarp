package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public DeleteCommand(MyWarp plugin)
    {
        super("Delete");
        this.plugin = plugin;
        setDescription("Deletes the warp §9<name>");
        setUsage("/warp delete §9<name>");
        setArgumentRange(1, 255);
        setIdentifiers("delete", "remove");
        setPermission("mywarp.warp.basic.delete");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            plugin.getWarpList().deleteWarp(StringUtils.join(args, ' '), (Player) executor);
        }
        else {
            executor.sendMessage("Console cannot delete warps for themselves!");
        }

        return true;
    }
}
