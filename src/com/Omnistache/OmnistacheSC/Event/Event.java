package com.Omnistache.OmnistacheSC.Event;

import org.bukkit.util.config.Configuration;

/**
 * An event, all events must handle their own timing and may use cooldowns
 * FYI, when all events are on cooldown, a world will return to the default
 * spawn settings while NOT trying to start new events
 * @author Nicholas.Brozack
 *
 */
public interface Event {
	
	public boolean isOnCooldown();
	
	public boolean isComplete();
	
	public void startEvent();
	
	public void dispose();
	
	public int initializeFromConfiguration(Configuration configuration);
}
