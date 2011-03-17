package com.Omnistache.OmnistacheSC; 

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
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
	public static Logger logger;

	private EventController eventController = null;

	public void onEnable() { 

		logger = getServer().getLogger();
		OmnistacheSCConfiguration.INSTANCE.initialize(this);
		//start up the event controller
		restartEventController();
		
		PluginDescriptionFile description = this.getDescription();
		logger.info(description.getName() + ", version: " + description.getVersion() + " is enabled!"); 
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

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}
} 
