package com.Omnistache.OmnistacheSC.Event;

import org.bukkit.util.config.Configuration;

public interface Event {
	
	public boolean isOnCooldown();
	
	public boolean isComplete();
	
	public void startEvent();
	
	public void dispose();
	
	public int initializeFromConfiguration(Configuration configuration);
}
