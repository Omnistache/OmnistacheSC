package com.Omnistache.OmnistacheSC.Spawn.Group;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.Omnistache.OmnistacheSC.Spawn.Group.AI.AI;
import com.Omnistache.OmnistacheSC.Spawn.Group.Style.SpawnStyle;

/*
 * Contains information regarding a group of spawned entities
 * Holds pointers to the actual living entities in the game world
 * Also contains AI information
 * while active, an internal schedule reinforces the group with new mobs
 */
public class SpawnGroup implements Runnable {

	private HashSet<LivingEntity> livingEntities = null;
	private World world;
	private int groupSize;
	private SpawnStyle spawnStyle;
	private BukkitScheduler scheduler;
	private Plugin plugin;
	private long reinforceDelay;
	private int reinforceTaskId = -1;
	private AI groupAI;
	private int reinforceAmount;
	private EntityModifier spawnModifier;
	
	public SpawnGroup(Plugin plugin, World world, SpawnStyle spawnStyle, EntityModifier spawnModifier,
			int groupSize, int reinforceDelay, AI groupAI, int reinforceAmount){
		this.world = world;
		this.spawnStyle = spawnStyle;
		this.groupSize = groupSize;
		this.livingEntities = new HashSet<LivingEntity>(groupSize);
		this.scheduler = plugin.getServer().getScheduler();
		this.plugin = plugin;
		this.reinforceDelay = reinforceDelay;
		this.reinforceAmount = reinforceAmount;
		this.groupAI = groupAI;
		this.spawnModifier = spawnModifier;
	}
	
	/*
	 * removes entities with 0 health
	 * run by two threads when group is active 
	 */
	private void removeDeadEntities(){
		
		ArrayList<LivingEntity> remove = new ArrayList<LivingEntity>();
		
		synchronized(livingEntities){
			for(LivingEntity ent : livingEntities){
				if(ent.getHealth() == 0){
					remove.add(ent);
				}
			}
			livingEntities.removeAll(remove);
		}
		
	}

	/*
	 * attempts to spawn reinforceAmount of mobs up to the
	 * groupSize
	 */
	private void reinforce() {
		removeDeadEntities();
		int openSpots = groupSize - livingEntities.size();
		//get the number to try to spawn
		int spawnCount = Math.min(openSpots, reinforceAmount);
		
		for(int i = 0; i < spawnCount; i++){
			LivingEntity entity = spawnStyle.spawnEntity(world);
			if(entity == null){
				continue;
			}
			else {
				synchronized(livingEntities){
					livingEntities.add(entity);
				}
				spawnModifier.applyToEntity(entity);
			}
		}
	}
	
	/*
	 * checks to see if the reinforce schedule is active
	 * which would mean that the group is actively
	 * spawning its mobs
	 */
	public boolean isActive(){
		return (reinforceTaskId != -1);
	}
	
	/*
	 * turns off the internal reinforce timer WITH A VENGEANCE
	 */
	public void deactivateGroup(){
		if(reinforceTaskId != -1){
			scheduler.cancelTask(reinforceTaskId);
			reinforceTaskId = -1;
		}
	}
	/*
	 * starts the internal reinforce timer if it is not running
	 */
	public void activateGroup(){
		if(reinforceTaskId == -1)
			reinforceTaskId  = scheduler.scheduleAsyncRepeatingTask(plugin, this, 0, reinforceDelay);
	}
	
	/*
	 * tick method for EntityController to tick the AI or something
	 */
	public void tickAI(){
		
		removeDeadEntities();

		synchronized(livingEntities){
			for(LivingEntity ent : livingEntities){
				groupAI.runAI(ent);
			}
		}
	}

	@Override
	public void run() {
		reinforce();
	}
	
}
