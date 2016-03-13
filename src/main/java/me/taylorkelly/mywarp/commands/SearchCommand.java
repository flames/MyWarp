package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;
import me.taylorkelly.mywarp.data.Searcher;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SearchCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public SearchCommand(MyWarp plugin)
    {
        super("Search");
        this.plugin = plugin;
        setDescription("Searches for warps related to §9<query>");
        setUsage("/warp search §9<query>");
        setArgumentRange(1, 255);
        setIdentifiers("search");
        setPermission("mywarp.warp.basic.search");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            Searcher searcher = new Searcher(plugin.getWarpList());
            searcher.addPlayer((Player) executor);
            searcher.setQuery(StringUtils.join(args, ' '));
            searcher.search();
        }
        else {
            executor.sendMessage("Console cannot search warps for themselves!");
        }

        return true;
    }

}
