package com.Omnistache.OmnistacheSC.Spawn.Group.Style;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.config.Configuration;

public interface SpawnStyle {

	/*
	 * Tries to spawn an entity,
	 * if not successful, returns null
	 */
	public LivingEntity spawnEntity(World world);
	
	public int initializeFromConfiguration(Configuration configuration);
}