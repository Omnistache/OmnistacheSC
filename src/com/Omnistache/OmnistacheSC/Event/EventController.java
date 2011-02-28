package com.Omnistache.OmnistacheSC.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.World;

import com.Omnistache.OmnistacheSC.OmnistacheSC;
import com.Omnistache.OmnistacheSC.OmnistacheSCConfiguration;
import com.Omnistache.OmnistacheSC.Spawn.Control.EntityController;

/*
 * Selects Events from the configuration and schedule them to run
 * in each world where there are events to run.
 * Also makes sure EntityControllers are running for each world
 * that has a configuration file
 * schedules its own task
 */
public class EventController implements Runnable {

	private Server server;
	private int eventControllerTaskId = -1;
	private OmnistacheSC plugin;
	private HashMap<World, EntityController> entityControllers;
	private HashMap<World, ArrayList<Event>> activeEvents;

	public EventController(Server server, OmnistacheSC plugin) {
		this.plugin = plugin;
		this.server = server;
		entityControllers = new HashMap<World, EntityController>(server.getWorlds().size() * 2);
		enableTask();
	}
	
	public void enableTask(){
		//it schedules itself
		if(eventControllerTaskId == -1){
			eventControllerTaskId = server.getScheduler().scheduleAsyncRepeatingTask(plugin, this, 20, OmnistacheSC.EVENT_CYCLE);
		}
	}
	
	public void disableTask(){
		server.getScheduler().cancelTask(eventControllerTaskId);
		eventControllerTaskId = -1;
	}
	
	/**
	 * checks to make sure the world has an EntityController if it needs one
	 * and removes unneeded EntityControllers
	 * @param world
	 */
	private void validateEntityController(World world){
		
		if(OmnistacheSCConfiguration.INSTANCE.hasConfig(world)){
			if(!entityControllers.containsKey(world)){
				//if the world has a configuration, but no EntityController, make a new one
				//this inserts the static spawn groups from the configuration file
				entityControllers.put(world, OmnistacheSCConfiguration.INSTANCE.entityControllerFromConfiguration(world));
			}
			else{
				//things are in order when the world with a configuration has an EntityController
			}
		}
		else {
			if(entityControllers.containsKey(world)){
				//if there is no configuration for the world, but an EntityController is running for some reason, remove it
				//this can happen on a reload of the configuration
				entityControllers.get(world).stopAndRemove();
				entityControllers.remove(world);
			}
			else{
				//things are in order when the world WITHOUT a configuration has NO EntityController
			}
		}
	}

	@Override
	public void run() {
		cycleEventController();
	}
	
	public void cycleEventController() {
		
		//grab a thread-safe snapshot of the world list
		List<World> worlds = server.getWorlds();
		ArrayList<World> worldsSafe;
		synchronized(worlds){
			worldsSafe = new ArrayList<World>(worlds);
		}
		
		for(World world : worldsSafe){
			//make sure EntityController is running
			validateEntityController(world);

			/* Check the current event state of the world
			 * None - make sure entityController is running the default phase settings and try
			 * 		to start a new event
			 * Event is running - do nothing, event takes care of itself
			 */

			//TODO: attempt to start events in each world
		}

	}
}
