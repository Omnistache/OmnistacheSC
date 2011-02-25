package com.Omnistache.OmnistacheSC.Spawn.Group;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import com.Omnistache.OmnistacheSC.Spawn.Control.EntityController;
import com.Omnistache.OmnistacheSC.Spawn.Group.Style.SpawnStyle;

/*
 * Contains information regarding a group of spawned entities
 * Holds pointers to the actual living entities in the game world
 * Also contains AI information
 */
public class SpawnGroup {

	private HashSet<LivingEntity> livingEntities = null;
	private World world;
	private int groupSize;
	private SpawnStyle spawnStyle;
	
	public SpawnGroup(World world, SpawnStyle spawnStyle){
		this.world = world;
		this.spawnStyle = spawnStyle;
	}	
		
	private void reinforce() {
		
	}
	
	/*
	 * turns off the internal reinforce timer WITH A VENGEANCE
	 */
	public void deactivateGroup(){
		
	}
	
	/*
	 * tick method for EntityController to tick the AI
	 */
	public void tickAI(){
		
	}
	
}
