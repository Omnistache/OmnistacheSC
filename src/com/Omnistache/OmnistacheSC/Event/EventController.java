package com.Omnistache.OmnistacheSC.Event;

import org.bukkit.Server;


/*
 * Selects Events from the configuration
 */
public class EventController implements Runnable {

	private Server server;

	public EventController(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		
	}

}
