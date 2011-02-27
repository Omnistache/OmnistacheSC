package com.Omnistache.OmnistacheSC.Spawn.Control;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.Omnistache.OmnistacheSC.OmnistacheSC;
import com.Omnistache.OmnistacheSC.Spawn.Group.SpawnGroup;

/*
 * EntityController, lives inside EventController
 * There is one of these for each world inside EventController
 * that has a configuration file
 * schedules its own task
 */
public class EntityController implements Runnable {

	private World world;

	private ArrayList<SpawnGroup> spawnGroups = new ArrayList<SpawnGroup>();
	private ArrayList<SpawnGroup> addSpawnGroups = new ArrayList<SpawnGroup>();
	private ArrayList<SpawnGroup> removeSpawnGroups = new ArrayList<SpawnGroup>();
	private int entityControllerTaskId = -1;

	private Plugin plugin;

	private MobSet unwantedMobs = MobSet.None;
	
	public EntityController(Plugin plugin, World world) {
		this.plugin = plugin;
		this.world = world;
		enableTask();
	}
	
	public void setUnwantedMobs(MobSet unwantedMobs){
		this.unwantedMobs = unwantedMobs;
	}
	
	public void enableTask(){
		if(entityControllerTaskId == -1){
			entityControllerTaskId = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, this, 20, OmnistacheSC.ENTITY_CONTROL_CYCLE);
		}
	}
	
	public void disableTask(){
		plugin.getServer().getScheduler().cancelTask(entityControllerTaskId);
		entityControllerTaskId = -1;
	}
	
	private void addNewGroups(){
		synchronized(addSpawnGroups){
			for(SpawnGroup addGroup : addSpawnGroups){
				spawnGroups.add(addGroup);
			}
			addSpawnGroups.clear();
		}
	}
	
	/*
	 * check groups for removal (empty and deactivated)
	 * static groups are always active until marked for removal
	 * or manually deactivated
	 */
	private void flagDeadGroups(){
		synchronized(removeSpawnGroups){

			for(SpawnGroup spawnGroup : spawnGroups){
				if(spawnGroup.isGroupDead()){
					removeSpawnGroups.add(spawnGroup);
				}
			}
		}
	}
	
	private void removeFlaggedGroups() {
		synchronized(removeSpawnGroups){
			//remove the groups flagged for removal
			for(SpawnGroup removeGroup : removeSpawnGroups){
				spawnGroups.remove(removeGroup);
				removeGroup.deactivateGroup(); //precaution to prevent lingering groups with no access to them
				//the above line prevents groups that reinforce themselves without AI running or the ability to turn them off
			}
			removeSpawnGroups.clear();
		}
	}
	
	@Override
	public void run() {
		
		//only run if players are logged in and in the world
		if(!playersInWorld())
			return;

		addNewGroups();
		
		flagDeadGroups();
		
		removeFlaggedGroups();
		
		//get a snapshot of the living entities in the world (to prevent concurrent modification)
		List<LivingEntity> livingEntities = world.getLivingEntities();
		ArrayList<LivingEntity> livingEntitiesSafe;
		synchronized(livingEntities){
			livingEntitiesSafe = new ArrayList<LivingEntity>(livingEntities);
		}

		//kill the unwanted/unprotected entities (mark them for removal)
		//livingEntitiesSafe is separate from the actual world entity list, so it won't have concurrent modification
		//when we kill the entities (and the server removes them from the world)
		for(LivingEntity livingEntity : livingEntitiesSafe){
			if(unwantedMobs.match(livingEntity)){ //if it's one of the mobs we're removing
				if(!isEntityInAGroup(livingEntity)){ //and it's not in a group we're watching
					livingEntity.remove(); //DESTROY IT
				}
			}
		}
		
		//tickAI on all the groups
		for(SpawnGroup spawnGroup : spawnGroups){
			spawnGroup.tickAI();
		}

	}

	private boolean playersInWorld() {
		for(Player player : plugin.getServer().getOnlinePlayers()){
			if(player.getWorld().equals(world)){ //this should work
				return true;
			}
		}
		plugin.getServer().getLogger().info("no players in world " + world.getName());
		return false;
	}

	private boolean isEntityInAGroup(LivingEntity livingEntity){
		/* INFO:
		 * spawnGroups is only used in the run() method, so it does not need to be synchronized
		 * the only case where this is a problem is if the method does not complete before it is called again
		 * by the scheduler, therefore we schedule it to run every 10 ticks so there should be plenty of time for it to complete
		 */
		for(SpawnGroup spawnGroup : spawnGroups){ //this is linear time
			if(spawnGroup.contains(livingEntity)){ //this is constant time
				return true;
			}
		}
		return false;
	}
	
	/*
	 * even though this should only be called from one thread, the eventController, it needs to be
	 * synchronized since the run() method iterates through it
	 */
	public void addSpawnGroup(SpawnGroup spawnGroup){
		if(spawnGroup == null)
			return;
		
		synchronized(addSpawnGroups){
			addSpawnGroups.add(spawnGroup);
		}
	}
	
	public void removeSpawnGroup(SpawnGroup spawnGroup){
		if(spawnGroup == null)
			return;
		
		synchronized(removeSpawnGroups){
			removeSpawnGroups.add(spawnGroup);
		}
	}

	public void stopAndRemove() {
		disableTask();
		synchronized(spawnGroups){ //precaution if the run method isn't finished in the off chance it was started just before the task was disabled
			for(SpawnGroup spawnGroup : spawnGroups){
				spawnGroup.stopAndRemove();
			}
		}
		
	}

}