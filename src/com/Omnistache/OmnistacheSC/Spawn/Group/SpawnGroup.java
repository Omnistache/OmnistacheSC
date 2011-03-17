package com.Omnistache.OmnistacheSC.Spawn.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.ConfigurationNode;

import com.Omnistache.OmnistacheSC.OmnistacheSC;
import com.Omnistache.OmnistacheSC.Spawn.Group.AI.AI;
import com.Omnistache.OmnistacheSC.Spawn.Group.AI.AIFactory;
import com.Omnistache.OmnistacheSC.Spawn.Group.Style.SpawnStyle;
import com.Omnistache.OmnistacheSC.Spawn.Group.Style.SpawnStyleFactory;

/*
 * Contains information regarding a group of spawned entities
 * Holds pointers to the actual living entities in the game world
 * Also contains AI information
 * while active, an internal schedule reinforces the group with new mobs
 */
public class SpawnGroup implements Runnable {

	private static final int DEFAULT_GROUP_SIZE = 10;

	private static final int MAX_GROUP_SIZE = 100;

	private static List<SpawnGroup> spawnGroups = new ArrayList<SpawnGroup>();

	private final String name;
	private HashSet<LivingEntity> livingEntities = null;
	private World world;
	private int groupSize;
	private SpawnStyle spawnStyle;
	private Plugin plugin;
	private long reinforceDelay;
	private int reinforceTaskId = -1;
	private AI groupAI;
	private int reinforceAmount;
	private EntityModifier entityModifier;
	
	public SpawnGroup(Plugin plugin, World world, SpawnStyle spawnStyle, EntityModifier entityModifier,
			int groupSize, int reinforceDelay, AI groupAI, int reinforceAmount, final String name){
		this.world = world;
		this.spawnStyle = spawnStyle;
		this.groupSize = groupSize;
		this.livingEntities = new HashSet<LivingEntity>(groupSize);
		this.plugin = plugin;
		this.reinforceDelay = reinforceDelay;
		this.reinforceAmount = reinforceAmount;
		this.groupAI = groupAI;
		this.entityModifier = entityModifier;
		this.name = name;
		addGroup(this);
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
		
		//try to spawn the bros
		for(int i = 0; i < spawnCount; i++){
			LivingEntity entity = spawnStyle.spawnEntity(world);
			if(entity == null){
				continue;
			}
			else {
				synchronized(livingEntities){
					livingEntities.add(entity);
				}
				if(entityModifier != null){
					entityModifier.applyToEntity(entity);
				}
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
	 * checks to see if there are any living entities remaining
	 * in the group
	 */
	public boolean isEmpty(){
		removeDeadEntities();
		return (livingEntities.size() == 0);
	}
	
	public boolean isGroupDead(){
		return (!isActive() && isEmpty());
	}
	
	/*
	 * turns off the internal reinforce timer WITH A VENGEANCE
	 */
	public void deactivateGroup(){
		if(reinforceTaskId != -1){
			plugin.getServer().getScheduler().cancelTask(reinforceTaskId);
			reinforceTaskId = -1;
		}
	}
	/*
	 * starts the internal reinforce timer if it is not running
	 */
	public void activateGroup(){
		if(reinforceTaskId == -1){
			reinforceTaskId = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(
					plugin, this, 0, reinforceDelay);
		}
	}
	
	/*
	 * tick method for EntityController to tick the AI or something
	 */
	public void tickAI(){
		
		removeDeadEntities();

		if(groupAI == null){
			return;
		}
		
		synchronized(livingEntities){
			for(LivingEntity ent : livingEntities){
				groupAI.runAI(ent);
			}
		}
	}
	
	/*
	 * checks to see if the LivingEntity is part of this group 
	 */
	public boolean contains(LivingEntity livingEntity){
		return livingEntities.contains(livingEntity);
	}

	@Override
	public void run() {
		reinforce();
	}

	public void stopAndRemove() {
		deactivateGroup();
		removeGroup(this);
		synchronized(livingEntities){
			livingEntities.clear();
			livingEntities = null;
		}
		world = null;
		plugin = null;
		entityModifier.disableAndFree();
	}
	
	public static void addGroup(SpawnGroup spawnGroup){
		spawnGroups.add(spawnGroup);
	}
	
	public static void removeGroup(SpawnGroup spawnGroup){
		spawnGroups.remove(spawnGroup);
	}
	
	public static SpawnGroup getFirstGroup(String name){
		for(SpawnGroup spawnGroup : spawnGroups){
			if(spawnGroup.name.equals(name)){
				return spawnGroup;
			}
		}
		return null;
	}
	
	public static List<SpawnGroup> getGroups(String name){
		List<SpawnGroup> spawnGroupList = new ArrayList<SpawnGroup>();
		for(SpawnGroup spawnGroup : spawnGroups){
			if(spawnGroup.name.equals(name)){
				spawnGroupList.add(spawnGroup);
			}
		}
		return spawnGroupList;
	}
	
	public static SpawnGroup fromConfiguration(ConfigurationNode configuration, String name, World world, Plugin plugin){
		
		Logger logger = OmnistacheSC.logger;
		
		if(configuration == null){
			logger.info("Could not create spawn group " + name + ", configuration missing");
			return null;
		}
		
		int reinforceAmount = configuration.getInt("reinforceAmount", 2);
		int groupSize = configuration.getInt("groupSize", DEFAULT_GROUP_SIZE);
		int reinforceDelay = configuration.getInt("reinforceDelay", 1000);
		
		if(reinforceDelay < 20){
			reinforceDelay = 20;
			logger.info("reinforceDelay cannot be less than 20 (once per second), so it has been set to 20");
		}
		
		if(groupSize <= 0){
			logger.info("Invalid groupSize: " + groupSize + ", setting to default (" + DEFAULT_GROUP_SIZE + ")");
			groupSize = DEFAULT_GROUP_SIZE;
		}
		
		if(groupSize > 100){
			logger.info("Invalid groupSize: " + groupSize + ", too large, setting to max (" + MAX_GROUP_SIZE + ")");
			groupSize = MAX_GROUP_SIZE;
		}
		
		ConfigurationNode spawnStyleNode = configuration.getNode("SpawnStyle");
		ConfigurationNode entityModifierNode = configuration.getNode("EntityModifier");
		ConfigurationNode groupAINode = configuration.getNode("GroupAI");
		
		SpawnStyle spawnStyle = SpawnStyleFactory.fromConfiguration(spawnStyleNode);
		if(spawnStyle == null){
			logger.info("Could not create SpawnGroup " + name + ", invalid/missing SpawnStyle");
			return null;
		}
		
		EntityModifier entityModifier = EntityModifier.fromConfiguration(entityModifierNode, groupSize, plugin);
		
		AI groupAI = AIFactory.fromConfiguration(groupAINode);
		
		SpawnGroup spawnGroup = new SpawnGroup(plugin, world, spawnStyle, entityModifier, groupSize, reinforceDelay, groupAI, reinforceAmount, name);
		return spawnGroup;
	}	
}
