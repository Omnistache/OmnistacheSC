package com.Omnistache.OmnistacheSC; 

import java.io.*; 
import java.util.HashMap; 
import java.util.logging.Logger;

import org.bukkit.Server; 
import org.bukkit.entity.Player; 
import org.bukkit.event.Event; 
import org.bukkit.event.Event.Priority; 
import org.bukkit.plugin.PluginDescriptionFile; 
import org.bukkit.plugin.PluginLoader; 
import org.bukkit.plugin.PluginManager; 
import org.bukkit.plugin.java.JavaPlugin;

import com.Omnistache.OmnistacheSC.Event.EventController;

/** 
 * OmnistacheZA for Bukkit 
 * 
 * @author Brozack_Nicholas.Brozack 
 */ 
public class OmnistacheSC extends JavaPlugin {
	
	public static final long EVENT_CYCLE = 100;
	private final OmnistacheSCEntityListener entityListener = new OmnistacheSCEntityListener(this); 
	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	
	private EventController eventController = null;

	public OmnistacheSC(PluginLoader pluginLoader, Server instance, 
			PluginDescriptionFile desc, File folder, File plugin, 
			ClassLoader cLoader) throws IOException { 
		super(pluginLoader, instance, desc, folder, plugin, cLoader); 

		//Load the configuration files
		OmnistacheSCConfiguration.INSTANCE.initialize(this);
	} 

	public void onEnable() { 
		// Register our events 
		PluginManager pm = getServer().getPluginManager(); 
		
		//For the prevention of zombies/skeletons bursting into flame as configured in the configuration file
		pm.registerEvent(Event.Type.ENTITY_COMBUST, entityListener, Priority.Normal, this);
		
		//start up the event controller
		restartEventController();
		
		PluginDescriptionFile description = this.getDescription();
		getServer().getLogger().info(description.getName() + ", version: " + description.getVersion() + " is enabled!"); 
	}
	
	public void onDisable() { 

	} 
	
	public boolean isDebugging(final Player player) { 
		if (debugees.containsKey(player)) { 
			return (Boolean) debugees.get(player); 
		} else { 
			return false; 
		} 
	}

	public void restartEventController(){
		if(eventController != null){
			eventController.disable();
		}
		eventController = new EventController(getServer(), this);
	}

	public void setDebugging(final Player player, final boolean value) { 
		debugees.put(player, value); 
	} 
} 
