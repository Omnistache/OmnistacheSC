package com.Omnistache.OmnistacheSC.Event;

import org.bukkit.Server;

import com.Omnistache.OmnistacheSC.OmnistacheSC;


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

	public EventController(Server server, OmnistacheSC plugin) {
		this.plugin = plugin;
		this.server = server;
		enable();
	}
	
	public void enable(){
		//it schedules itself
		if(eventControllerTaskId == -1){
			eventControllerTaskId = server.getScheduler().scheduleAsyncRepeatingTask(plugin, this, 20, OmnistacheSC.EVENT_CYCLE);
		}
	}
	
	public void disable(){
		server.getScheduler().cancelTask(eventControllerTaskId);
		eventControllerTaskId = -1;
	}

	@Override
	public void run() {
		//make sure entityControllers are running
		
		//make sure they have the correct static spawn groups
		
		//attempt to start events in each world
		
		//
	}

}
