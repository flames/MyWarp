package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PublicCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public PublicCommand(MyWarp plugin)
    {
        super("Public");
        this.plugin = plugin;
        setDescription("Make §9<name>§e a public warp");
        setUsage("/warp public §9<name>");
        setArgumentRange(1, 255);
        setIdentifiers("public");
        setPermission("mywarp.warp.soc.public");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {

        if (executor instanceof Player) {
            plugin.getWarpList().publicize(StringUtils.join(args, ' '), (Player) executor);
        }
        else {
            executor.sendMessage("Console cannot make warps public for themselves!");
        }

        return true;
    }
}
