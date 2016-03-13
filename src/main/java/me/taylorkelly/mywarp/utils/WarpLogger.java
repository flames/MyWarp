package me.taylorkelly.mywarp.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WarpLogger {

    private static String name = "MyWarp";
    private static final Logger log = Logger.getLogger("Minecraft");

    public static void severe(String string, Exception ex) {
        log.log(Level.SEVERE, format(string), ex);
    }

    public static void severe(String string) {
        log.severe(format(string));
    }

    public static void info(String string) {
        log.info(format(string));
    }

    public static void warning(String string) {
        log.warning(format(string));
    }

    public static String format(String msg) {
        return String.format("[%s] %s", name, msg);
    }
}
