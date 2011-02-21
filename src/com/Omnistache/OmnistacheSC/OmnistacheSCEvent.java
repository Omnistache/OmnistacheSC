package com.Omnistache.OmnistacheSC;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.World;

public class OmnistacheSCEvent {

	private String name = null;
	private ArrayList<OmnistacheSCPhase> phases = new ArrayList<OmnistacheSCPhase>();
	private int[] begins = null;
	private int probability = 100;
	private int minimumPlayersOnline = 1;
	private ArrayList<String> occursInWorlds = null;
	private OmnistacheSCPhase currentPhase = null;
	private World world = null;
	
	public boolean isAllowedInWorld(World world){
		if(occursInWorlds == null)
			return true;
		
		if(occursInWorlds.isEmpty())
			return true;
		
		if(occursInWorlds.contains("All"))
			return true;
		
		if(occursInWorlds.contains(world.getName()))
			return true;
		
		//test for general terms like NETHER or NORMAL
		if(occursInWorlds.contains(world.getEnvironment().toString()))
			return true;
		
		return false;
	}
	
	public boolean testProbability(){
		
		//Shazzam.
		if(probability > (new Random()).nextInt(99)){
			return true;
		}
		return false;
	}
}
