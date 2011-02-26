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
	private int taskId = -1;

	public EventController(Server server, OmnistacheSC plugin) {
		this.server = server;
		//it schedules itself
		taskId = server.getScheduler().scheduleAsyncRepeatingTask(plugin, this, 100, OmnistacheSC.EVENT_CYCLE);
	}
	
	public void disable(){
		server.getScheduler().cancelTask(taskId);
		server = null;
		taskId = -1;
		//TODO: cancel all other tasks associated FFFUUUUUU clean up loose ends avoid memory LEEEEAKS!!!!
	}

	@Override
	public void run() {
		
	}

}
