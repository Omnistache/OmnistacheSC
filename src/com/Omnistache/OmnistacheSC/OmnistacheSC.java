package com.Omnistache.OmnistacheSC; 

import java.io.File;
import java.io.IOException;

import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import com.Omnistache.OmnistacheSC.Event.EventController;

/** 
 * OmnistacheSC for Bukkit 
 * 
 * @author Brozack_Nicholas.Brozack 
 */ 
public class OmnistacheSC extends JavaPlugin {
	
	public static final long EVENT_CYCLE = 100;
	public static final long ENTITY_CONTROL_CYCLE = 10;

	private EventController eventController = null;

	public OmnistacheSC(PluginLoader pluginLoader, Server instance, 
			PluginDescriptionFile desc, File folder, File plugin, 
			ClassLoader cLoader) throws IOException { 
		super(); 

		//Load the configuration files
		OmnistacheSCConfiguration.INSTANCE.initialize(this);
	} 

	public void onEnable() { 

		//start up the event controller
		restartEventController();
		
		PluginDescriptionFile description = this.getDescription();
		getServer().getLogger().info(description.getName() + ", version: " + description.getVersion() + " is enabled!"); 
	}
	
	public void onDisable() {
		//TODO: Clean up EVERYTHING

	} 
	
	public void restartEventController(){
		if(eventController != null){
			eventController.disableTask();
		}
		eventController = new EventController(getServer(), this);
	}
} 
