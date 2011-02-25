package com.Omnistache.OmnistacheSC.Event;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.World;


public class Event {

	private String name = null;
	private ArrayList<Phase> phases = new ArrayList<Phase>();
	private int[] begins = null;
	private int probability = 100;
	private int minimumPlayersOnline = 1;
	private ArrayList<String> occursInWorlds = null;
	private Phase currentPhase = null;
	private World world = null;
	
	public boolean testProbability(){
		
		//Shazzam.
		if(probability > (new Random()).nextInt(99)){
			return true;
		}
		return false;
	}
}
