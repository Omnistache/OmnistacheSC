package com.Omnistache.OmnistacheSC.Event;

import org.bukkit.Server;

import com.Omnistache.OmnistacheSC.OmnistacheSC;


/*
 * Selects Events from the configuration and schedule them to run
 * in each world where there are events to run.
 * Also makes sure EntityControllers are running for each world
 * that has a configuration file
 */
public class EventController implements Runnable {

	private Server server;
	private int eventControllerTaskId = -1;

	public EventController(Server server, OmnistacheSC plugin) {
		this.server = server;
		//it schedules itself
		eventControllerTaskId = server.getScheduler().scheduleAsyncRepeatingTask(plugin, this, 20, OmnistacheSC.EVENT_CYCLE);
	}
	
	public void disable(){
		server.getScheduler().cancelTask(eventControllerTaskId);
		server = null;
		eventControllerTaskId = -1;
		//TODO: cancel all other tasks associated FFFUUUUUU clean up loose ends avoid memory LEEEEAKS!!!!
	}

	@Override
	public void run() {
		//make sure entitycontrollers are running
		
		//make sure they have the correct static spawn groups
		
		//attempt to start events in each world
		
		//
	}

}
