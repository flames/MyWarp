package me.taylorkelly.mywarp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WarpLogger {

	private final Logger logger;
	private final String pluginName;
	private static WarpLogger instance;
	
	public synchronized static WarpLogger getLogger(){
		if (instance == null)
		{
			instance = new WarpLogger("MyWarp");
		}
		return instance;
	}

	private WarpLogger(String loggerName, String pluginName) {
		this.logger = Logger.getLogger(loggerName);
		this.pluginName = pluginName;
	}

	private WarpLogger(String pluginName) {
		this("minecraft", pluginName);
	}

	private String formatMessage(String message) {
		return "[" + pluginName + "]: " + message;
	}

	public void info(String msg) {
		this.logger.info(this.formatMessage(msg));
	}

	public void warning(String msg) {
		this.logger.warning(this.formatMessage(msg));
	}

	public void severe(String msg) {
		this.logger.severe(this.formatMessage(msg));
	}

	public void severe(String msg, Throwable exception) {
		this.log(Level.SEVERE, msg, exception);
	}

	public void log(Level level, String msg, Throwable exception) {
		this.logger.log(level, this.formatMessage(msg), exception);
	}
	public void log(Level level, String msg) {
		this.logger.log(level, this.formatMessage(msg));
	}

}