package me.taylorkelly.mywarp.commands;

import java.util.Arrays;

import me.taylorkelly.mywarp.MyWarp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public GiveCommand(MyWarp plugin)
    {
        super("Give");
        this.plugin = plugin;
        setDescription("Give your warp §9<name>§e to §8<player>");
        setUsage("/warp give §8<player> §9<name>");
        setArgumentRange(2, 255);
        setIdentifiers("give");
        setPermission("mywarp.warp.soc.give");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            Player givee = plugin.getServer().getPlayer(args[0]);
            // TODO Change to matchPlayer
            String giveeName = (givee == null) ? args[0] : givee.getName();

            plugin.getWarpList().give(StringUtils.join(Arrays.asList(args).subList(1, args.length), ' '), (Player) executor, giveeName);
        }
        else {
            executor.sendMessage("Console cannot give warps from themselves!");
        }

        return true;
    }
}
