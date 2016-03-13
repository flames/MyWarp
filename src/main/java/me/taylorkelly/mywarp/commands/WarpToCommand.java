package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpToCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public WarpToCommand(MyWarp plugin)
    {
        super("WarpTo");
        this.plugin = plugin;
        setDescription("Warp to §9<name>");
        setUsage("/warp §9<name>");
        setArgumentRange(1, 255);
        setIdentifiers("warp", "mywarp", "mw");
        setPermission("mywarp.warp.basic.warp");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            plugin.getWarpList().warpTo(StringUtils.join(args, ' '), (Player) executor);
        }
        else {
            executor.sendMessage("Console cannot warp to locations!");
        }

        return true;
    }

}
