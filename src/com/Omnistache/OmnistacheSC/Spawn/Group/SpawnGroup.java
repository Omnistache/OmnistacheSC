package com.Omnistache.OmnistacheSC.Spawn.Group;

import java.util.ArrayList;

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

	private ArrayList<LivingEntity> livingEntities = new ArrayList<LivingEntity>();
	private World world;
	private SpawnStyle spawnStyle;

	
	public SpawnGroup(World world, SpawnStyle spawnStyle){
		this.world = world;
		this.spawnStyle = spawnStyle;
	}	
		
	public void reinforce() {
		
	}

	public void deactivateGroup(){
		
	}
	
	/*
	 * tick method for EntityController to tick the AI
	 * this checks to see if there are any mobs still alive
	 * if there are not, it sends a notification to the controller
	 * to remove itself
	 */
	public void tickAI(EntityController controller){
		
	}
	
}
