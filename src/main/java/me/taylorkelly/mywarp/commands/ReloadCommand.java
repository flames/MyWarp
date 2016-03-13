package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;
import me.taylorkelly.mywarp.WarpSettings;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public ReloadCommand(MyWarp plugin)
    {
        super("Reload");
        this.plugin = plugin;
        setDescription("Reload settings");
        setUsage("/warp reload");
        setArgumentRange(0, 0);
        setIdentifiers("reload");
        setPermission("mywarp.admin");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        WarpSettings.initialize(plugin);
        executor.sendMessage("Reloading MyWarp config");

        return true;
    }

}
