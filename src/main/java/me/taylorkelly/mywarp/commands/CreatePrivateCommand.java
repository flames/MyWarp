package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreatePrivateCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public CreatePrivateCommand(MyWarp plugin)
    {
        super("pcreate");
        this.plugin = plugin;
        setDescription("Create a new private warp called §9<name>");
        setUsage("/warp pcreate §9<name>");
        setArgumentRange(1, 255);
        setIdentifiers("pcreate");
        setPermission("mywarp.warp.basic.createprivate");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            plugin.getWarpList().addWarpPrivate(StringUtils.join(args, ' '), (Player) executor);
        }
        else {
            executor.sendMessage("Console cannot create private warps for themselves!");
        }

        return true;
    }
}
