package me.taylorkelly.mywarp.commands;

import me.taylorkelly.mywarp.MyWarp;
import me.taylorkelly.mywarp.data.Lister;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends BasicCommand implements Command
{
    private MyWarp plugin;

    public ListCommand(MyWarp plugin)
    {
        super("List");
        this.plugin = plugin;
        setDescription("List the warps you can visit");
        setUsage("/warp list §8[owner] §8[page#]");
        setArgumentRange(0, 2);
        setIdentifiers("list");
        setPermission("mywarp.warp.basic.list");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        if (executor instanceof Player) {
            Lister lister = new Lister(plugin.getWarpList());
            lister.addPlayer((Player) executor);

            if (args.length == 0) {
                lister.setPage(1);
            } else if (args.length == 1) {
                if (args[0].matches("-?\\d+(\\.\\d+)?")) {
                    int page = Integer.parseInt(args[0]);
                    if (page < 1) {
                        executor.sendMessage(ChatColor.RED
                                + "Page number can't be below 1.");
                        return true;
                    } else if (page > lister.getMaxPages((Player) executor)) {
                        executor.sendMessage(ChatColor.RED + "There are only "
                                + lister.getMaxPages((Player) executor)
                                + " pages of warps");
                        return true;
                    }
                    lister.setPage(page);
                } else {
                    if (args[0].equals("own")) {
                        lister.setWarpCreator(executor.getName());
                    } else {
                        lister.setWarpCreator(args[0]);
                    }
                    lister.setPage(1);
                }
            } else if (args.length == 2) {
                String creator;
                if (args[0].equals("own")) {
                    creator = executor.getName();
                } else {
                    creator = args[0];
                }
                lister.setWarpCreator(creator);
                int page = Integer.parseInt(args[1]);
                if (page < 1) {
                    executor.sendMessage(ChatColor.RED + "Page number can't be below 1.");
                    return true;
                } else if (page > lister.getMaxPagesPerCreator((Player) executor, creator)) {
                    executor.sendMessage(ChatColor.RED + "There are only "
                            + lister.getMaxPagesPerCreator((Player) executor, executor.getName()) + " pages of warps");
                    return true;
                }
                lister.setPage(page);
            } else {
                return false;
            }
            lister.list();
        } else {
            executor.sendMessage("Console cannot list warps for themselves!");
        }
        return true;
    }
}
