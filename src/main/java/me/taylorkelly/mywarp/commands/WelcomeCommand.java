package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WelcomeCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public WelcomeCommand(MyWarp plugin)
    {
        super("Welcome");
        this.plugin = plugin;
        setDescription("Change the welcome message of §9<name>");
        setUsage("/warp welcome §9<name>");
        setArgumentRange(1, 255);
        setIdentifiers("welcome");
        setPermission("mywarp.warp.basic.welcome");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            plugin.getWarpList().welcomeMessage(StringUtils.join(args, ' '), (Player) executor);
        }
        else {
            executor.sendMessage("Console cannot change warp welcome messages for themselves!");
        }

        return true;
    }
}
