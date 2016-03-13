package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrivateCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public PrivateCommand(MyWarp plugin)
    {
        super("Private");
        this.plugin = plugin;
        setDescription("Make §9<name>§e a private warp");
        setUsage("/warp private §9<name>");
        setArgumentRange(1, 255);
        setIdentifiers("private");
        setPermission("mywarp.warp.soc.private");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            plugin.getWarpList().privatize(StringUtils.join(args, ' '), (Player) executor);
        }
        else {
            executor.sendMessage("Console cannot make warps private for themselves!");
        }

        return true;
    }
}
