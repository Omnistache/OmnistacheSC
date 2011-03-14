package com.Omnistache.OmnistacheSC.Event;

public interface Event {
	
	public boolean isOnCooldown();
	
	public boolean isComplete();
	
	public void startEvent();
	
	public void dispose();
	
}
