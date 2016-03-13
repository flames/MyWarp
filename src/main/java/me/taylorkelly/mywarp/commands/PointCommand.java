package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PointCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public PointCommand(MyWarp plugin)
    {
        super("Point");
        this.plugin = plugin;
        setDescription("Point your compass to §9<name>");
        setUsage("/warp point §9<name>");
        setArgumentRange(1, 255);
        setIdentifiers("point");
        setPermission("mywarp.warp.basic.compass");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            plugin.getWarpList().point(StringUtils.join(args, ' '), (Player) executor);
        }
        else {
            executor.sendMessage("Console cannot search warps for themselves!");
        }

        return true;
    }
}
