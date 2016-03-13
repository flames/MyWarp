package me.taylorkelly.mywarp.commands;

import java.util.ArrayList;
import java.util.List;

import me.taylorkelly.mywarp.MyWarp;

import org.bukkit.command.CommandSender;

public class HelpCommand extends BasicCommand implements Command
{
    private static final int CMDS_PER_PAGE = 8;
    private MyWarp plugin;

    public HelpCommand(MyWarp plugin)
    {
        super("Help");
        this.plugin = plugin;
        setDescription("Displays the help menu");
        setUsage("/warp help §8[page#]");
        setArgumentRange(0, 1);
        setIdentifiers("help");
    }

    @Override
    public boolean execute(CommandSender executor, String identifier, String[] args)
    {
        int page = 0;
        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            }
            catch (NumberFormatException e) {
            }
        }

        List<Command> sortCommands = plugin.getCommandHandler().getCommands();
        List<Command> commands = new ArrayList<Command>();

        // Build list of permitted commands
        for (Command command : sortCommands) {
            if (command.isShownOnHelpMenu()) {
                if (plugin.getCommandHandler().hasPermission(executor, command.getPermission())) {
                    commands.add(command);
                }
            }
        }

        int numPages = commands.size() / CMDS_PER_PAGE;
        if (commands.size() % CMDS_PER_PAGE != 0) {
            numPages++;
        }

        if (page >= numPages || page < 0) {
            page = 0;
        }
        executor.sendMessage("§c-----[ " + "§fMyWarp Help <" + (page + 1) + "/" + numPages + ">§c ]-----");
        int start = page * CMDS_PER_PAGE;
        int end = start + CMDS_PER_PAGE;
        if (end > commands.size()) {
            end = commands.size();
        }
        for (int c = start; c < end; c++) {
            Command cmd = commands.get(c);
            executor.sendMessage("  §a" + cmd.getUsage());
        }

        executor.sendMessage("§cFor more info on a particular command, type §f/<command> ?");

        return true;
    }
}
