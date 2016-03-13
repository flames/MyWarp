package me.taylorkelly.mywarp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.taylorkelly.mywarp.commands.AdminWarpToCommand;
import me.taylorkelly.mywarp.commands.CommandHandler;
import me.taylorkelly.mywarp.commands.CreateCommand;
import me.taylorkelly.mywarp.commands.CreatePrivateCommand;
import me.taylorkelly.mywarp.commands.DeleteCommand;
import me.taylorkelly.mywarp.commands.GiveCommand;
import me.taylorkelly.mywarp.commands.HelpCommand;
import me.taylorkelly.mywarp.commands.InviteCommand;
import me.taylorkelly.mywarp.commands.ListCommand;
import me.taylorkelly.mywarp.commands.PointCommand;
import me.taylorkelly.mywarp.commands.PrivateCommand;
import me.taylorkelly.mywarp.commands.PublicCommand;
import me.taylorkelly.mywarp.commands.ReloadCommand;
import me.taylorkelly.mywarp.commands.ListAllCommand;
import me.taylorkelly.mywarp.commands.SearchCommand;
import me.taylorkelly.mywarp.commands.UninviteCommand;
import me.taylorkelly.mywarp.commands.WarpToCommand;
import me.taylorkelly.mywarp.commands.WelcomeCommand;
import me.taylorkelly.mywarp.data.WarpList;
import me.taylorkelly.mywarp.griefcraft.Updater;
import me.taylorkelly.mywarp.listeners.MWBlockListener;
import me.taylorkelly.mywarp.listeners.MWPlayerListener;
import me.taylorkelly.mywarp.permissions.WarpPermissions;
import me.taylorkelly.mywarp.sql.ConnectionManager;
import me.taylorkelly.mywarp.utils.WarpLogger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MyWarp extends JavaPlugin {

    private WarpList warpList;
    private MWPlayerListener playerListener;
    private MWBlockListener blockListener;
    public String name;
    public String version;
    private Updater updater;
    private PluginManager pm;
    private CommandHandler commandHandler;
    private static WarpPermissions warpPermissions;

    @Override
    public void onDisable() {
        ConnectionManager.closeConnection();
    }

    @Override
    public void onEnable() {
        name = this.getDescription().getName();
        version = this.getDescription().getVersion();
        pm = getServer().getPluginManager();
        
        WarpSettings.initialize(this);
        
        libCheck();
        if(!sqlCheck()) { return; }

        File newDatabase = new File(getDataFolder(), "warps.db");
        File oldDatabase = new File("homes-warps.db");
        if (!newDatabase.exists() && oldDatabase.exists()) {
            updateFiles(oldDatabase, newDatabase);
        }

        Connection conn = ConnectionManager.initialize();
        if (conn == null) {
            WarpLogger.severe("Could not establish SQL connection. Disabling MyWarp");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        warpList = new WarpList(getServer());
        warpPermissions = new WarpPermissions(this);        
        blockListener = new MWBlockListener(this);
        playerListener = new MWPlayerListener(this);

        pm.registerEvents(blockListener, this);
        pm.registerEvents(playerListener, this);

        commandHandler = new CommandHandler(this);
        
        // basic commands
        commandHandler.addCommand(new CreateCommand(this));
        commandHandler.addCommand(new CreatePrivateCommand(this));
        commandHandler.addCommand(new DeleteCommand(this));
        commandHandler.addCommand(new ListCommand(this));
        commandHandler.addCommand(new ListAllCommand(this));
        commandHandler.addCommand(new PointCommand(this));
        commandHandler.addCommand(new SearchCommand(this));
        commandHandler.addCommand(new WelcomeCommand(this));
        commandHandler.addCommand(new WarpToCommand(this));

        // social commands
        commandHandler.addCommand(new GiveCommand(this));
        commandHandler.addCommand(new InviteCommand(this));
        commandHandler.addCommand(new PrivateCommand(this));
        commandHandler.addCommand(new PublicCommand(this));
        commandHandler.addCommand(new UninviteCommand(this));

        // help command
        commandHandler.addCommand(new HelpCommand(this));

        // admin commands
        commandHandler.addCommand(new AdminWarpToCommand(this));
        commandHandler.addCommand(new ReloadCommand(this));

        WarpLogger.info(name + " " + version + " enabled");
    }


    private void libCheck(){
        updater = new Updater();
        try {
            updater.check();
            updater.update();
        } catch (Exception e) {
        }
    }
    
    private boolean sqlCheck() {
        Connection conn = ConnectionManager.initialize();
        if (conn == null) {
            WarpLogger.severe("Could not establish SQL connection. Disabling MyWarp");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        } 
        return true;
    }
    
    private void updateFiles(File oldDatabase, File newDatabase) {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        if (newDatabase.exists()) {
            newDatabase.delete();
        }
        try {
            newDatabase.createNewFile();
        } catch (IOException ex) {
        	WarpLogger.severe("Could not create new database file", ex);
        }
        copyFile(oldDatabase, newDatabase);
    }

    /**
     * File copier from xZise
     * @param fromFile
     * @param toFile
     */
    private static void copyFile(File fromFile, File toFile) {
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            Logger.getLogger(MyWarp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException e) {
                }
            }
            if (to != null) {
                try {
                    to.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        return commandHandler.dispatch(sender, command, commandLabel, args);
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public static WarpPermissions getWarpPermissions() {
        return warpPermissions;
    }
    
    public WarpList getWarpList() {
        return warpList;
    }
    
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}
