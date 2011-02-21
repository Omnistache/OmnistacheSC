package com.Omnistache.OmnistacheSC;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class OmnistacheSCPhase {

	private String name = null;
	
	private ArrayList<LivingEntity> removeMobs = new ArrayList<LivingEntity>();
	private int setHealth = 20;
	private long duration = 0;
	private long startTime = 0;
	private String startMessage;
	private String endMessage;
	private String[] timedMessage;
	private long timedMessageIncrement;
	private World world = null;

	public long remainingTime(){
		return world.getFullTime() - startTime + duration;
	}
}
