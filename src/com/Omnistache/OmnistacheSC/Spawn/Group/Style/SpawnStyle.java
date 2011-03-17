package com.Omnistache.OmnistacheSC.Spawn.Group.Style;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.config.Configuration;

/**
 * SpawnStyle interface, create ways to spawn enemies
 * this must have an associated type in the configuration
 * register with SpawnStyleFactory using a static block
 * static {
 * 	SpawnStyleFactory.register(type-name, <? extends SpawnStyle> class)
 * @author Nicholas.Brozack
 *
 */
public interface SpawnStyle {

	/*
	 * Tries to spawn an entity,
	 * if not successful, returns null
	 */
	public LivingEntity spawnEntity(World world);
	
	public int initializeFromConfiguration(Configuration configuration);
}