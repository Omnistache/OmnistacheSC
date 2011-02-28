package com.Omnistache.OmnistacheSC.Spawn.Group.Style;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public interface SpawnStyle {

	/*
	 * Tries to spawn an entity,
	 * if not successful, returns null
	 */
	public LivingEntity spawnEntity(World world);
}