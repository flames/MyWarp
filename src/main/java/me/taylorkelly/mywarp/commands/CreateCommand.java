package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public CreateCommand(MyWarp plugin)
    {
        super("Create");
        this.plugin = plugin;
        setDescription("Create a new warp called §9<name>");
        setUsage("/warp create|set §9<name>");
        setArgumentRange(1, 255);
        setIdentifiers("create", "set");
        setPermission("mywarp.warp.basic.createpublic");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            plugin.getWarpList().addWarp(StringUtils.join(args, ' '), (Player) executor);
        }
        else {
            executor.sendMessage("Console cannot create warps for themselves!");
        }

        return true;
    }
}
