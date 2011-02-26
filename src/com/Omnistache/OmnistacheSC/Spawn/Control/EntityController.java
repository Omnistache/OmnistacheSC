package com.Omnistache.OmnistacheSC.Spawn.Control;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

/*
 * EntityController, lives inside EventController
 * There is one of these for each world inside EventController
 * that has a configuration file
 */
public class EntityController implements Runnable {

	private World world;

	public EntityController(World world) {
		this.world = world;
	}

	@Override
	public void run() {
		//get a snapshot of the living entities in the world (to prevent concurrent modification)
		List<LivingEntity> livingEntities = world.getLivingEntities();
		ArrayList<LivingEntity> livingEntitiesSafe;
		synchronized(livingEntities){
			livingEntitiesSafe = new ArrayList<LivingEntity>(livingEntities);
		}
		
		//update and check all the static groups
		
		//check non-static groups for removal (empty and deactivated)
		
		//get a snapshot of all the spawn groups (hashsets for the win)
		
		//flag the unwanted/unprotected entities (add them to a removal list)
		
		//kill the unwanted entities
		
		//tickAI on all the groups

	}

}